package com.playus.communityservice.domain.comment.dto.comment_create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CommentCreateRequest(

        @NotNull(message = "postId는 null일 수 없습니다!")
        Long postId,

        Long commentGroupId,

        @NotBlank(message = "댓글의 내용이 비어 있습니다!")
        @Size(min = 1, max = 100, message = "댓글의 내용을 1~100자 이내로 작성해 주세요!")
        String content
) {

    public static CommentCreateRequest of(Long postId, Long commentGroupId, String content) {
        return CommentCreateRequest.builder()
                .postId(postId)
                .commentGroupId(commentGroupId)
                .content(content)
                .build();
    }
}
