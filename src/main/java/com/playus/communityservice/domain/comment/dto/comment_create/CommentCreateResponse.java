package com.playus.communityservice.domain.comment.dto.comment_create;

import lombok.Builder;

@Builder
public record CommentCreateResponse(
        Long commentId,
        String message
) {

    public static CommentCreateResponse of(Long commentId, String message) {
        return CommentCreateResponse.builder()
                .commentId(commentId)
                .message(message)
                .build();
    }
}
