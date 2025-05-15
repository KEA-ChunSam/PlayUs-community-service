package com.playus.communityservice.domain.post.dto.post_update;

import lombok.Builder;

@Builder
public record PostUpdateResponse(
        boolean success,
        String message
) {
    public static PostUpdateResponse of(boolean success, String message) {
        return PostUpdateResponse.builder()
                .success(success)
                .message(message)
                .build();
    }
}
