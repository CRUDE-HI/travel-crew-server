package com.crude.travelcrew.global.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.crude.travelcrew.global.security.CustomUserDetails;
import com.crude.travelcrew.global.security.jwt.model.RefreshToken;
import com.crude.travelcrew.global.security.jwt.repository.BlockAccessTokenRepository;
import com.crude.travelcrew.global.security.jwt.repository.RefreshTokenRepository;
import com.crude.travelcrew.global.security.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {


	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;            // 30분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 5;      // 5일

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CustomUserDetailsService userDetailsService;
	private final BlockAccessTokenRepository blockAccessTokenRepository;


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
		CustomUserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			if (blockAccessTokenRepository.existsById(token)) {
				log.error("already logout access token");
				return false;
			}
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty");
		}
		return false;
	}


	public Long getExpiration(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration()
			.getTime();
	}

	public Long getRemainingTime(String token) {
		Date now = new Date();
		return now.getTime() - getExpiration(token);
	}

	public String resolveToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
			return authHeader.substring(BEARER_PREFIX.length());
		}
		return null;
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