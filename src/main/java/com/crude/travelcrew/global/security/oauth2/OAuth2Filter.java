package com.crude.travelcrew.global.security.oauth2;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import com.crude.travelcrew.global.error.exception.MemberException;
import com.crude.travelcrew.global.error.type.MemberErrorCode;
import com.crude.travelcrew.global.security.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2Filter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final HttpClient httpClient;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String token = jwtProvider.resolveToken(request);

		if (token != null && jwtProvider.validateToken(token)) {
			Authentication auth = jwtProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);
	}

	private JSONObject getToken(AuthProvider authProvider) throws Exception {
		StringBuilder builder = new StringBuilder(authProvider.getTokenUri());
		builder.append("?code=").append(authProvider.getCode())
			.append("&grant_type=").append(authProvider.getGrantType())
			.append("client_id=").append(authProvider.getClientId())
			.append("client_secret=").append(authProvider.getClientSecret())
			.append("redirect_uri").append(authProvider.getRedirectUri());

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(builder.toString()))
			.timeout(Duration.ofSeconds(5))
			.POST(HttpRequest.BodyPublishers.ofString(""))
			.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		int statusCode = response.statusCode();
		String responseBody = response.body();

		if (statusCode == 200) {
			JSONObject responseJSON = new JSONObject(responseBody);
			return responseJSON;
		} else {
			throw new MemberException(MemberErrorCode.INTERNAL_ERROR);
		}
	}

	private JSONObject getUserInfo(AuthProvider authProvider, String access_token) throws Exception {
		StringBuilder builder = new StringBuilder(authProvider.getUserInfoUri());

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(builder.toString()))
			.timeout(Duration.ofSeconds(5))
			.header(JwtProvider.getAuthorizationHeader(), JwtProvider.getBearerPrefix() + access_token)
			.GET()
			.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		int statusCode = response.statusCode();
		String responseBody = response.body();

		if (statusCode == 200) {
			JSONObject responseJSON = new JSONObject(responseBody);
			return responseJSON;
		} else {
			throw new MemberException(MemberErrorCode.INTERNAL_ERROR);
		}
	}

}
