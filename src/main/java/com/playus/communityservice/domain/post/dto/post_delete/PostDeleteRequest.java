package com.playus.communityservice.domain.post.dto.post_delete;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PostDeleteRequest(

        @NotNull(message = "postID는 null일 수 없습니다!")
        Long postId
) {
    public static PostDeleteRequest of(Long postId) {
        return PostDeleteRequest.builder()
                .postId(postId)
                .build();
    }
}
