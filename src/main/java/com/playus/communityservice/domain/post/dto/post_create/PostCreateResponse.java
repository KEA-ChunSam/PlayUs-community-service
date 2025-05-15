package com.playus.communityservice.domain.post.dto.post_create;

import lombok.Builder;

@Builder
public record PostCreateResponse(
        Long postId,
        String massage
) {

    public static PostCreateResponse of(Long postId, String massage) {
        return PostCreateResponse.builder()
                .postId(postId)
                .massage("게시물 생성이 완료되었습니다.")
                .build();
    }
}
