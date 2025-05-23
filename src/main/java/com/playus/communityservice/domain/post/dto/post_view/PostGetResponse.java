package com.playus.communityservice.domain.post.dto.post_view;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostGetResponse(
        Long postId,
        String title,
        LocalDate date,
        String writerNickname,
        String writerProfileImage,
        boolean activated,
        String image,
        String content,
        List<CommentDto> comments
) {
    public record CommentDto(
            Long commentId,
            String writerNickname,
            String writerProfileImage,
            boolean activated,
            String content,
            List<ReCommentDto> reComments
    ) {}

    public record ReCommentDto(
            Long reCommentId,
            String writerNickname,
            String writerProfileImage,
            boolean activated,
            String content
    ) {}
}
