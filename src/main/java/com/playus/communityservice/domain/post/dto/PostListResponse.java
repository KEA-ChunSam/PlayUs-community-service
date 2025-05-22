package com.playus.communityservice.domain.post.dto;

import java.time.LocalDate;

public record PostListResponse(
        String title,
        LocalDate date,
        String writerNickname,
        String image
) {}