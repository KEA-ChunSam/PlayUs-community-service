package com.playus.communityservice.domain.post.controller;

import com.playus.communityservice.ControllerTestSupport;
import com.playus.communityservice.domain.common.security.JwtUser;
import com.playus.communityservice.domain.common.security.Role;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest extends ControllerTestSupport {

    //    Long userId = 1L;
    String thumbnailUrl = "http://image.com";
//    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(applicantUserId, null, List.of(new SimpleGrantedAuthority("USER")));

    private UsernamePasswordAuthenticationToken token;

    @BeforeEach
    void setUp() {
        long userId = 1L;

        // 더미 OAuth2 사용자
        JwtUser principal = Mockito.mock(JwtUser.class);
        when(principal.getName()).thenReturn(Long.toString(userId));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.name()));
        doReturn(authorities).when(principal).getAuthorities();

        token = new UsernamePasswordAuthenticationToken(
                principal, null, authorities
        );
    }

    // 게시글 작성 Happy Case
    @DisplayName("게시글을 생성할 수 있다")
    @Test
    void createPost() throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.of("title",thumbnailUrl,"content",null,false);
        PostCreateResponse response = PostCreateResponse.of(1L,"message");
        given(postService.createGeneralPost(any(PostCreateRequest.class),any(JwtUser.class),any(TeamTag.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/post")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.message").value("message"));
    }

    // 게시글 작성 title 이슈
    @DisplayName("게시글 생성 중 제목은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "title = {0}")
    void createPostWithEmptyTitle(String emptyTitle) throws Exception {
        // given
        PostCreateRequest request = PostCreateRequest.of(emptyTitle,thumbnailUrl,"content",null,false);
        PostCreateResponse response = PostCreateResponse.of(1L,"message");
        given(postService.createGeneralPost(any(PostCreateRequest.class),any(JwtUser.class),any(TeamTag.class))).willReturn(response);

        // when // then
        assertBadRequestOfPostCreateRequest(request, "게시글의 제목이 비어 있습니다!");
    }

    private void assertBadRequestOfPostCreateRequest(PostCreateRequest request, String expectedResult) throws Exception {
        mockMvc.perform(post("/post")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(expectedResult));
    }

}