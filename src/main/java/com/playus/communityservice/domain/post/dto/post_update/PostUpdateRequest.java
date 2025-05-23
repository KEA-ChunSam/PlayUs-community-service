package com.playus.communityservice.domain.post.dto.post_update;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PostUpdateRequest(

        @NotNull(message = "postID는 null일 수 없습니다!")
        Long postId,

        @NotBlank(message = "게시글 제목이 비어 있습니다!")
        @Size(max = 225, message = "제목의 길이를 1~225자 이내로 작성해 주세요!")
        String title,

        String image,

        @NotBlank(message = "본문이 비어 있습니다!")
        @Size(max = 500, message = "본문의 길이를 1~500자 이내로 작성해 주세요!")
        String content,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate twpDate
) {

    public static PostUpdateRequest of(Long postId, String title, String image, String content, LocalDate twpDate) {
        return PostUpdateRequest.builder()
                .postId(postId)
                .title(title)
                .image(image)
                .content(content)
                .twpDate(twpDate)
                .build();
    }
}
