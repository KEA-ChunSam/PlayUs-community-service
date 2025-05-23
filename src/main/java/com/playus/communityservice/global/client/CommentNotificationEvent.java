package com.playus.communityservice.global.client;

import lombok.Builder;

@Builder
public record CommentNotificationEvent(
        Long commentId,
        Long postId,
        Long writerId,
        String content,
        boolean activated
) {
    public static CommentNotificationEvent of(Long commentId, Long postId, Long writerId, String content, boolean activated) {
        return CommentNotificationEvent.builder()
                .commentId(commentId)
                .postId(postId)
                .writerId(writerId)
                .content(content)
                .activated(activated)
                .build();
    }
}

