package com.crude.travelcrew.global.security.jwt;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.crude.travelcrew.global.security.jwt.model.RefreshToken;
import com.crude.travelcrew.global.security.jwt.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;            // 30분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 5;      // 5일

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final RefreshTokenRepository refreshTokenRepository;


	@PostConstruct
	protected void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	private static Map<String, Object> createJWTHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		header.put("alg", "HS256");
		return header;
	}

	public String createAccessToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);
		Date now = new Date();
		return Jwts.builder()
			.setHeader(createJWTHeader())
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String createRefreshToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);
		Date now = new Date();
		String token = Jwts.builder()
			.setHeader(createJWTHeader())
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();

		saveRefreshTokenInRedis(token, email);
		return token;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		// DB 통해서 EMAIL을 가지고 권한을 가져오는 로직 수행
		// EMAIL claims.getSubject()
		// 권한 Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
		return new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
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

	public void saveRefreshTokenInRedis(String token, String email) {
		RefreshToken refreshToken = RefreshToken.builder()
			.id(email)
			.token(token)
			.expiration(REFRESH_TOKEN_EXPIRE_TIME)
			.build();
		refreshTokenRepository.save(refreshToken);
	}
}