package com.playus.communityservice.domain.post.dto.diary_view;

import java.time.LocalDate;

public record DiaryListResponse(
        String title,
        String thumbnail,
        LocalDate date
) {}
