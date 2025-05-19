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
import com.playus.communityservice.global.config.jwt.JwtUser;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.global.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentGroupRepository commentGroupRepository;

    public CommentCreateResponse createComment(CommentCreateRequest request, JwtUser user) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        CommentGroup commentGroup;
        int order;

        if (request.commentId() == null) {
            commentGroup = CommentGroup.create(post);
            commentGroupRepository.save(commentGroup);
            order = 1;
        } else {
            Comment parent = commentRepository.findById(request.commentId())
                    .orElseThrow(() -> new EntityNotFoundException("부모 댓글"));
            commentGroup = parent.getCommentGroup();
            order = 2;
        }

        Comment comment = Comment.create(
                user.getId(),
                commentGroup,
                (long) order,
                request.content()
        );

        commentRepository.save(comment);
        return CommentCreateResponse.of(comment.getId(), "댓글 생성이 완료되었습니다.");
    }

    public CommentUpdateResponse updateComment(CommentUpdateRequest request, JwtUser user) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new EntityNotFoundException("댓글"));

        if (!comment.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("댓글");
        }

        comment.updateContent(request.content());
        return CommentUpdateResponse.of(true,"댓글이 수정되었습니다.");
    }

    public CommentDeleteResponse deleteComment(CommentDeleteRequest request, JwtUser user) {
        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new EntityNotFoundException("댓글"));

        if (!comment.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("댓글");
        }

        comment.delete();
        return CommentDeleteResponse.of(true, "댓글이 삭제되었습니다.");
    }
}
