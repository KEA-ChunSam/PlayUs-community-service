package com.playus.communityservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "userNotificationClient",
        url = "${feign.user.url}",
        path = "/user"
)
public interface UserNotificationClient {

    @DeleteMapping("/comment/{comment-id}")
    void deleteNotificationsByCommentId(@PathVariable("comment-id") Long commentId);
}

