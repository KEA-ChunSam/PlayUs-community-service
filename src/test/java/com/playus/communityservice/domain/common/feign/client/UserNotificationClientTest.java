package com.playus.communityservice.domain.common.feign.client;

import com.playus.communityservice.IntegrationTestSupport;
import com.playus.communityservice.global.client.UserNotificationClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNotificationClientTest extends IntegrationTestSupport {

    @Autowired
    UserNotificationClient userNotificationClient;

    @DisplayName("댓글을 작성하면 알림이 전송될 수 있다.")
    @Test
    void getUserNotification() {}
}
