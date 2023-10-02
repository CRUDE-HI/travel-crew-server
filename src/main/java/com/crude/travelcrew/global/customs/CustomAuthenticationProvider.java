package com.crude.travelcrew.global.customs;

import com.crude.travelcrew.global.entity.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails member = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

        if (bCryptPasswordEncoder.matches(password, member.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, password, member.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(Set<Authority> authorities) {
        Set<SimpleGrantedAuthority> list = new HashSet<>();
        for (Authority auth : authorities) {
            list.add(new SimpleGrantedAuthority(auth.getAuthority()));
        }
        return list;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
