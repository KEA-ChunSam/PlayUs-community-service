package com.playus.communityservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notificationClient",
        url = "${feign.user.url}",
        path = "/user/api"
)
public interface NotificationClient {

    @PostMapping("/notifications/comment")
    void notifyComment(@RequestBody CommentNotificationEvent event);
}

