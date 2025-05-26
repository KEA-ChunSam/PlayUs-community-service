package com.playus.communityservice.global.config.security;

import com.playus.communityservice.global.jwt.JwtFilter;
import com.playus.communityservice.global.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final String [] INTERNAL_PATHS = {
            // "/post/**",
            "/posts/**",
    };

    private String [] getWhiteList() {
        return new String[] {
                "/error",
                "/swagger",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**",
                "/api-docs",
                "/api-docs/**",
                "/v3/api-docs/**",
                "/oauth2/authorization/kakao",
                "/login/oauth2/code/kakao",
                "/oauth2/authorization/naver",
                "/login/oauth2/code/naver",
                "/api/v1/auth/reissue",
                "/api/v1/auth/logout",
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.cors((httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource)));
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 인증/인가
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(getWhiteList())
                .permitAll()
                .requestMatchers(INTERNAL_PATHS)
                .permitAll()
                .anyRequest().authenticated()
        );

        // 세션 관리: Stateless
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();

    }
}