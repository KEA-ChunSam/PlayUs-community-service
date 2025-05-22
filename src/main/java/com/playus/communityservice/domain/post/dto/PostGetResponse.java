package com.playus.communityservice.domain.post.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostGetResponse(
        String title,
        LocalDate date,
        String writerNickname,
        String writerProfileImage,
        boolean isExpert,
        String image,
        String content,
        List<CommentDto> comments
) {
    public record CommentDto(
            String writerNickname,
            String writerProfileImage,
            boolean isExpert,
            String content,
            List<ReCommentDto> reComments
    ) {}

    public record ReCommentDto(
            String writerNickname,
            String writerProfileImage,
            boolean isExpert,
            String content
    ) {}
}
