package com.playus.communityservice.domain.comment.dto.comment_create;

import lombok.Builder;

@Builder
public record CommentCreateResponse(
        Long commentId,
        Long userId,
        String message,
        Long commentGroupId,
        String content,
        String nickName,
        String image
) {

    public static CommentCreateResponse of(Long userId,Long commentId, Long commentGroupId,
                                           String message, String content, String nickName, String image) {
        return CommentCreateResponse.builder()
                .userId(userId)
                .commentId(commentId)
                .commentGroupId(commentGroupId)
                .message(message)
                .content(content)
                .nickName(nickName)
                .image(image)
                .build();
    }
}
