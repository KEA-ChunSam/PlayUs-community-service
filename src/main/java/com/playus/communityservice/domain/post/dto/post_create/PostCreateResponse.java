package com.playus.communityservice.domain.post.dto.post_create;

import lombok.Builder;

@Builder
public record PostCreateResponse(
        Long postId,
        String message
) {

    public static PostCreateResponse of(Long postId, String message) {
        return PostCreateResponse.builder()
                .postId(postId)
                .message(message)
                .build();
    }
}
