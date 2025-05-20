package com.playus.communityservice.domain.comment.controller;

import com.playus.communityservice.ControllerTestSupport;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateRequest;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateResponse;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteRequest;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteResponse;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateRequest;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateResponse;
import com.playus.communityservice.global.config.jwt.JwtUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

class CommentControllerTest extends ControllerTestSupport {

    private UsernamePasswordAuthenticationToken token;

    @BeforeEach
    void setUp() {
        JwtUser user = new JwtUser(1L, "user");

        token = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @DisplayName("댓글 생성에 성공한다.")
    @Test
    void createComment() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest(1L, 1L,1L,"댓글 내용");
        CommentCreateResponse response = new CommentCreateResponse(1L,"댓글이 생성되었습니다.");
        given(commentService.createComment(any(), any(JwtUser.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.message").value("댓글이 생성되었습니다."));
    }

    @DisplayName("댓글 수정에 성공한다.")
    @Test
    void updateComment() throws Exception {
        // given
        CommentUpdateRequest request = new CommentUpdateRequest(1L, "수정된 댓글");
        CommentUpdateResponse response = new CommentUpdateResponse(true, "댓글이 수정되었습니다.");
        given(commentService.updateComment(any(), any(JwtUser.class))).willReturn(response);

        // when // then
        mockMvc.perform(put("/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("댓글이 수정되었습니다."));
    }

    @DisplayName("댓글 삭제에 성공한다.")
    @Test
    void deleteComment() throws Exception {
        // given
        CommentDeleteRequest request = new CommentDeleteRequest(1L, 1L);
        CommentDeleteResponse response = new CommentDeleteResponse(true,"댓글이 삭제되었습니다.");
        given(commentService.deleteComment(any(), any(JwtUser.class))).willReturn(response);

        // when // then
        mockMvc.perform(patch("/comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("댓글이 삭제되었습니다."));
    }
}