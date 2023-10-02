package com.crude.travelcrew.global.security;

import com.crude.travelcrew.global.customs.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                // disable csrf token
                .csrf().disable()
                // disable cors policy
                .cors().disable()
                // set permission of endpoints
                .authorizeRequests(
                        (authorizeRequests) -> authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/admin/sign-up")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyRole("ROLE_ADMIN", "ROLE_MANAGER")
                                .requestMatchers(new AntPathRequestMatcher("/record/**")).authenticated()
                                .requestMatchers(new AntPathRequestMatcher("/chat/**")).authenticated()
                                .requestMatchers(new AntPathRequestMatcher("/my-page/**")).authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin().disable()
                // H2-console frameOption
                .headers().frameOptions().sameOrigin()
                .and()
                // disable session (because we use jwt token)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // use custom jwtAuthFilter and CorsFilter
                .addFilterBefore(new JwtAuthFilter(jwtProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

}