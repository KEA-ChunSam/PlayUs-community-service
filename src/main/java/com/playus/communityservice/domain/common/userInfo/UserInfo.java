package com.playus.communityservice.domain.common.userInfo;

public record UserInfo(
        String nickname,
        String profileImage
) {
    public static UserInfo from(UserReadService userReadService, Long userId) {
        try {
            UserInfoResponse user = userReadService.getUserInfo(userId);
            if (user == null) return new UserInfo("알 수 없음", "");

            return new UserInfo(
                    user.getNickname() != null ? user.getNickname() : "알 수 없음",
                    user.getThumbnailUrl() != null ? user.getThumbnailUrl() : ""
            );
        } catch (Exception e) {
            return new UserInfo("알 수 없음", "");
        }
    }
}
