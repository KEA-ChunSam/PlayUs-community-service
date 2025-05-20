package com.playus.communityservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playus.communityservice.domain.comment.controller.CommentController;
import com.playus.communityservice.domain.comment.service.CommentService;
import com.playus.communityservice.global.config.jwt.JwtUtil;
import com.playus.communityservice.global.exception.ExceptionAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
        CommentController.class
})
@Import({ControllerTestSupport.TestSecurityConfig.class, ExceptionAdvice.class})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected CommentService commentService;

    @MockitoBean
    protected JwtUtil jwtUtil;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            http.formLogin(AbstractHttpConfigurer::disable);
            http.httpBasic(AbstractHttpConfigurer::disable);
            http.authorizeHttpRequests(auth -> auth
                    .anyRequest().hasAuthority("USER")
            );

            // 세션 관리: Stateless
            http.sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

            return http.build();

        }
    }
}
