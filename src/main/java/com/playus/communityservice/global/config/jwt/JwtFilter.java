package com.playus.communityservice.global.config.jwt;

import com.playus.communityservice.global.config.jwt.JwtUser;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (!jwtUtil.isExpired(token)) {
                    Long userId = Long.parseLong(jwtUtil.getUserId(token));
                    String role = jwtUtil.getRole(token);

                    JwtUser jwtUser = new JwtUser(userId, role);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            jwtUser, null, jwtUser.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException | IllegalArgumentException e) {
                log.debug("JWT 인증 실패: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
