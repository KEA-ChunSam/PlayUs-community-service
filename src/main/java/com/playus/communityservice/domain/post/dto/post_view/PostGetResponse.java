package com.playus.communityservice.domain.post.dto.post_view;

import com.playus.communityservice.domain.post.enums.TeamTag;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostGetResponse(
        Long postId,
        TeamTag tag,
        String title,
        LocalDate date,
        String writerNickname,
        String writerProfileImage,
        String image,
        String content,
        List<CommentDto> comments
) {
    public record CommentDto(
            Long commentId,
            Long commentGroupId,
            String writerNickname,
            String writerProfileImage,
            String content,
            List<ReCommentDto> reComments
    ) {}

    public record ReCommentDto(
            Long reCommentId,
            Long commentGroupId,
            String writerNickname,
            String writerProfileImage,
            String content
    ) {}
}
