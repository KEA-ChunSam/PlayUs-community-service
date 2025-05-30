package com.playus.communityservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "userNotificationClient",
        url = "${feign.user.url}",
        path = "/user/api"
)
public interface UserNotificationClient {

    @PostMapping("/notifications/comment")
    void notifyComment(@RequestBody CommentNotificationEvent event);

    @DeleteMapping("/comment/{comment-id}")
    void deleteNotificationsByCommentId(@PathVariable("comment-id") Long commentId);
}

