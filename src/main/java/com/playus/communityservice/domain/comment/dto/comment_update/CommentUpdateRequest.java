package com.playus.communityservice.domain.comment.dto.comment_update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CommentUpdateRequest(

        @NotNull(message = "commentId는 null일 수 없습니다!")
        Long commentId,

        @NotNull(message =  "commentGroupID는 null일 수 없습니다.")
        Long commentGroupId,

        @NotBlank(message = "댓글의 내용이 비어 있습니다!")
        @Size(min = 1, max = 100, message = "댓글의 내용을 1~100자 이내로 작성해 주세요!")
        String content
) {

    public static CommentUpdateRequest of(Long commentId,Long commentGroupId, String content) {
        return CommentUpdateRequest.builder()
                .commentId(commentId)
                .commentGroupId(commentGroupId)
                .content(content)
                .build();
    }
}
