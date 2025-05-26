package com.playus.communityservice.domain.comment.dto.comment_create;

import lombok.Builder;

@Builder
public record CommentCreateResponse(
        Long commentId,
        String message,
        Long commentGroupId
) {

    public static CommentCreateResponse of(Long commentId, Long commentGroupId, String message) {
        return CommentCreateResponse.builder()
                .commentId(commentId)
                .commentGroupId(commentGroupId)
                .message(message)
                .build();
    }
}
