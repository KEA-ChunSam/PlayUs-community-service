package com.playus.communityservice.domain.comment.dto.comment_create;

import lombok.Builder;

@Builder
public record CommentCreateResponse(
        Long commentId,
        Long userId,
        String message,
        Long commentGroupId,
        String content
) {

    public static CommentCreateResponse of(Long userId,Long commentId, Long commentGroupId, String message, String content) {
        return CommentCreateResponse.builder()
                .userId(userId)
                .commentId(commentId)
                .commentGroupId(commentGroupId)
                .message(message)
                .content(content)
                .build();
    }
}
