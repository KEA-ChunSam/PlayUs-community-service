package com.playus.communityservice.domain.post.dto.diary_view;

import com.playus.communityservice.domain.post.enums.TeamTag;

import java.time.LocalDate;

public record DiaryListResponse(
        Long postId,
        String title,
        String thumbnail,
        LocalDate date,
        TeamTag TeamName
) {}
