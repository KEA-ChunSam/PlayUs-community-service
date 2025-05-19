package com.playus.communityservice.domain.comment.service;

import com.playus.communityservice.IntegrationTestSupport;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateRequest;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateResponse;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteRequest;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteResponse;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateRequest;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateResponse;
import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.comment.entity.CommentGroup;
import com.playus.communityservice.domain.comment.repository.write.CommentGroupRepository;
import com.playus.communityservice.domain.comment.repository.write.CommentRepository;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.repository.write.PostRepository;
import com.playus.communityservice.global.config.jwt.JwtUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentGroupRepository commentGroupRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        commentGroupRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
    }

    @DisplayName("최상위 댓글을 생성할 수 있다")
    @Test
    void createParentComment() {
        // given
        Long userId = 100L;
        JwtUser jwtUser = new JwtUser(userId, null);
        Post post = saveTestPost(userId);

        CommentCreateRequest request = new CommentCreateRequest(post.getId(), null, "댓글 내용");

        // when
        CommentCreateResponse response = commentService.createComment(request, jwtUser);

        // then
        assertThat(commentRepository.count()).isEqualTo(1);
        assertThat(commentGroupRepository.count()).isEqualTo(1);

        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getUserId()).isEqualTo(userId);
        assertThat(comment.getCommentOrder()).isEqualTo(1);
        assertThat(comment.getContent()).isEqualTo("댓글 내용");
    }

    @DisplayName("대댓글을 생성할 수 있다")
    @Test
    void createChildComment() {
        // given
        Long userId = 100L;
        JwtUser jwtUser = new JwtUser(userId, null);
        Post post = saveTestPost(userId);

        CommentGroup group = commentGroupRepository.save(CommentGroup.create(post));
        Comment parent = commentRepository.save(
                Comment.create(userId, group, post.getId(), "부모 댓글")
        );

        CommentCreateRequest request = new CommentCreateRequest(post.getId(), parent.getId(), "대댓글");

        // when
        CommentCreateResponse response = commentService.createComment(request, jwtUser);

        // then
        assertThat(commentRepository.count()).isEqualTo(2);

        Comment child = commentRepository.findById(response.commentId()).orElseThrow();
        assertThat(child.getUserId()).isEqualTo(userId);
        assertThat(child.getCommentOrder()).isEqualTo(2);
        assertThat(child.getCommentGroup().getId()).isEqualTo(parent.getCommentGroup().getId());
        assertThat(child.getContent()).isEqualTo("대댓글");
    }

    @DisplayName("댓글을 수정할 수 있다")
    @Test
    void updateComment() {
        // given
        Long userId = 100L;
        JwtUser jwtUser = new JwtUser(userId, null);
        Post post = saveTestPost(userId);

        CommentGroup group = commentGroupRepository.save(CommentGroup.create(post));
        Comment comment = commentRepository.save(
                Comment.create(userId, group, post.getId(), "수정 전")
        );

        CommentUpdateRequest request = new CommentUpdateRequest(comment.getId(), "수정 후");

        // when
        CommentUpdateResponse response = commentService.updateComment(request, jwtUser);

        // then
        assertThat(response.message()).isEqualTo("댓글이 수정되었습니다.");
        Comment updated = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("수정 후");
    }

    @DisplayName("댓글을 삭제할 수 있다")
    @Test
    void deleteComment() {
        // given
        Long userId = 100L;
        JwtUser jwtUser = new JwtUser(userId, null);

        Post post = saveTestPost(userId);

        CommentGroup group = commentGroupRepository.save(CommentGroup.create(post));
        Comment comment = commentRepository.save(
                Comment.create(userId, group, post.getId(), "삭제할 댓글")
        );

        CommentDeleteRequest request = new CommentDeleteRequest(comment.getId());

        // when
        CommentDeleteResponse response = commentService.deleteComment(request, jwtUser);

        // then
        assertThat(response.message()).isEqualTo("ok");
        Comment deleted = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(deleted.isActivated()).isFalse();
    }

    private Post saveTestPost(Long writerId) {
        return postRepository.save(
                Post.create(writerId, "제목", "본문", TeamTag.DOOSAN_BEARS, LocalDate.parse("2025-05-06"))
        );
    }
}