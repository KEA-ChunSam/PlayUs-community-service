package com.playus.communityservice.domain.comment.dto.comment_delete;

import lombok.Builder;

@Builder
public record CommentDeleteResponse(
        boolean success,
        String message
) {

    public static CommentDeleteResponse of(boolean success, String message) {
        return CommentDeleteResponse.builder()
                .success(success)
                .message(message)
                .build();
    }
}
