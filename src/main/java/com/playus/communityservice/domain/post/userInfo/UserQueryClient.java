package com.playus.communityservice.domain.post.userInfo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "userQueryClient",
        url = "${feign.user.url}",
        path = "/user/api"
)
public interface UserQueryClient {

    @PostMapping("/info")
    List<PartyApplicantsInfoFeignResponse> getUserInfos(@RequestBody List<Long> userIdList);
}

