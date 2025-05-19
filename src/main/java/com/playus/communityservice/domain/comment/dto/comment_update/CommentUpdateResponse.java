package com.playus.communityservice.domain.comment.dto.comment_update;

import lombok.Builder;

@Builder
public record CommentUpdateResponse(
        boolean success,
        String message
) {

    public static CommentUpdateResponse of(boolean success, String message) {
        return CommentUpdateResponse.builder()
                .success(success)
                .message(message)
                .build();
    }
}
