package com.playus.communityservice.domain.comment.dto.comment_update;

import lombok.Builder;

@Builder
public record CommentUpdateResponse(
        boolean success,
        String message,
        Long userId,
        String content,
        String image,
        String nickName
) {

    public static CommentUpdateResponse of(boolean success, String message, Long userId, String content, String nickName, String image) {
        return CommentUpdateResponse.builder()
                .success(success)
                .message(message)
                .userId(userId)
                .content(content)
                .nickName(nickName)
                .image(image)
                .build();
    }
}
