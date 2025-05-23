package com.playus.communityservice.domain.post.dto.diary_view;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiaryGetResponse(
        String title,
        LocalDate date,
        String image,
        String content
) {
}
