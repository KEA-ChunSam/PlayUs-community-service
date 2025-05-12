package com.playus.communityservice.domain.post.dto.post_update;

public record PostUpdateResponse(
        boolean success,
        String message
) {
    public static PostUpdateResponse ok() {
        return new PostUpdateResponse(true, "게시글이 수정되었습니다.");
    }
}
