package com.crude.travelcrew.global.security;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtProvider {
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;            // 30분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24;      // 1일
	private final Key key;

	public JwtProvider(@Value("${jwt.secret.key}") String secretKey) {
		byte[] keyBytes = new byte[512];
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createAccessToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String createRefreshToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		return new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
			Collections.singleton(new SimpleGrantedAuthority(getRole(token))));
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public String getRole(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			log.debug(e.getMessage());
			return false;
		}
	}

	public long getExpiration(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration()
			.getTime();
	}

	public String resolveToken(HttpServletRequest request) {
		// 수정사항 2. Postman에서 요청 시에 Authorization Header 내 Value에 "Bearer " 값을 prepend하여 보냄.
		// 아래 코드에 맞춰서 Postman 툴에서 Authorization Header 내에 "Bearer " 값을 삭제시키고 보내도록 수정.
		return request.getHeader("Authorization");
	}

}