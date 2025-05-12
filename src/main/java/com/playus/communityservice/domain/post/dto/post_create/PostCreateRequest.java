package com.playus.communityservice.domain.post.dto.post_create;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record PostCreateRequest(

        @NotBlank(message = "게시글 제목이 비어 있습니다!")
        @Size(max = 225, message = "제목의 길이를 1~225자 이내로 작성해 주세요!")
        String title,

        @Size(max = 255)
        String image,

        @NotBlank(message = "본문이 비어 있습니다!")
        @Size(max = 500, message = "본문의 길이를 1~500자 이내로 작성해 주세요!")
        String message

) {

    public static PostCreateRequest of(String title, String image , String message) {
        return PostCreateRequest.builder()
                .title(title)
                .image(image)
                .message(message)
                .build();
    }
}
