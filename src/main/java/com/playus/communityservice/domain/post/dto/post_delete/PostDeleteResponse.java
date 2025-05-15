package com.playus.communityservice.domain.post.dto.post_delete;

import lombok.Builder;

@Builder
public record PostDeleteResponse(
        boolean success,
        String message
) {
    public static PostDeleteResponse of(boolean success, String message) {
        return PostDeleteResponse.builder()
                .success(true)
                .message("게시물이 삭제되었습니다.")
                .build();
    }
}
