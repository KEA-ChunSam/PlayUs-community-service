package com.playus.communityservice.domain.comment.service;

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
import com.playus.communityservice.domain.post.repository.write.PostRepository;
import com.playus.communityservice.domain.common.userInfo.UserInfo;
import com.playus.communityservice.domain.common.userInfo.UserReadService;
import com.playus.communityservice.global.client.CommentNotificationEvent;
import com.playus.communityservice.global.client.UserNotificationClient;
import com.playus.communityservice.domain.common.security.JwtUser;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.global.exception.ForbiddenAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentGroupRepository commentGroupRepository;
    private final UserNotificationClient userNotificationClient;
    private final UserReadService userReadService; // ✅ 추가

    public CommentCreateResponse createComment(CommentCreateRequest request, JwtUser user) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        CommentGroup commentGroup;
        int order;

        if (request.commentGroupId() == null) {
            commentGroup = CommentGroup.create(post);
            commentGroupRepository.save(commentGroup);
            order = 1;
        } else {
            commentGroup = commentGroupRepository.findById(request.commentGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("댓글 그룹"));

            boolean hasActiveComments = commentRepository.existsByCommentGroupAndActivatedTrue(commentGroup);
            if (!hasActiveComments) {
                throw new IllegalArgumentException("삭제된 댓글 그룹에는 답글을 작성할 수 없습니다.");
            }

            order = 2;
        }

        if (request.content() == null || request.content().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }

        Comment comment = Comment.create(
                user.getId(),
                commentGroup,
                order == 1 ? 1L : 2L,
                request.content()
        );

        commentRepository.save(comment);

        // 알림 전송
        CommentNotificationEvent event = CommentNotificationEvent.of(
                comment.getId(),
                post.getId(),
                comment.getUserId(),
                comment.getContent(),
                comment.isActivated()
        );
        try {
            userNotificationClient.notifyComment(event);
        } catch (Exception e) {
            log.warn("댓글 알림 전송 실패: commentId={}, error={}", comment.getId(), e.getMessage());
        }

        // ✅ 작성자 정보 가져오기
        UserInfo userInfo = UserInfo.from(userReadService, user.getId());

        return CommentCreateResponse.of(
                user.getId(),
                comment.getId(),
                comment.getCommentGroup().getId(),
                "댓글 생성이 완료되었습니다.",
                comment.getContent(),
                userInfo.nickname(),
                userInfo.profileImage()
        );
    }

    public CommentUpdateResponse updateComment(CommentUpdateRequest request, JwtUser user) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new EntityNotFoundException("댓글"));

        if (!comment.getUserId().equals(user.getId())) {
            throw new ForbiddenAccessException("댓글");
        }

        if (request.content() == null || request.content().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }

        comment.updateContent(request.content());

        // ✅ 작성자 정보 가져오기
        UserInfo userInfo = UserInfo.from(userReadService, user.getId());

        return CommentUpdateResponse.of(
                true,
                "댓글이 수정되었습니다.",
                user.getId(),
                comment.getContent(),
                userInfo.nickname(),
                userInfo.profileImage()
        );
    }

    public CommentDeleteResponse deleteComment(CommentDeleteRequest request, JwtUser user) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new EntityNotFoundException("댓글"));

        if (!comment.getUserId().equals(user.getId())) {
            throw new ForbiddenAccessException("댓글");
        }

        if (comment.getCommentOrder() == 1) {
            // 부모 댓글이면 해당 그룹의 모든 댓글 비활성화
            CommentGroup group = comment.getCommentGroup();
            commentRepository.findAllByCommentGroup(group)
                    .forEach(Comment::delete);
        } else {
            // 자식 댓글은 개별 삭제
            comment.delete();
        }

        // 알림 삭제 요청 (실패해도 댓글 비활성화에 영향 없음)
        try {
            userNotificationClient.deleteNotificationsByCommentId(comment.getId());
        } catch (Exception e) {
            log.warn("알림 삭제 실패: commentId={}, error={}", comment.getId(), e.getMessage());
        }

        return CommentDeleteResponse.of(true, "댓글이 삭제되었습니다.");
    }
}
