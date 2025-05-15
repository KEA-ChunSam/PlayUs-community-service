package com.playus.communityservice.domain.post.dto.post_update;

import lombok.Builder;

@Builder
public record PostUpdateResponse(
        boolean success,
        String message
) {
    public static PostUpdateResponse fo(boolean success, String message) {
        return PostUpdateResponse.builder()
                .success(true)
                .message("게시물이 수정되었습니다.")
                .build();
    }
}
