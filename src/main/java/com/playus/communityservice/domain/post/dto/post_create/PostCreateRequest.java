package com.playus.communityservice.domain.post.dto.post_create;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record PostCreateRequest(

        @NotBlank(message = "게시글 제목이 비어 있습니다!")
        @Size(min= 1, max = 225, message = "제목의 길이를 1~225자 이내로 작성해 주세요!")
        String title,

        String image,

        @NotBlank(message = "본문이 비어 있습니다!")
        @Size(min = 1, max = 500, message = "본문의 길이를 1~500자 이내로 작성해 주세요!")
        String content,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate jwpDate,

        boolean isSecret

) {

    public static PostCreateRequest of(String title, String image , String content, LocalDate jwpDate, boolean isSecret) {
        return PostCreateRequest.builder()
                .title(title)
                .image(image)
                .content(content)
                .jwpDate(jwpDate)
                .isSecret(isSecret)
                .build();
    }

    public void validate() {
        if (isSecret && jwpDate == null) {
            throw new IllegalArgumentException("직관일지의 경우 jwpDate는 필수입니다.");
        }
    }
}
