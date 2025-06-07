package com.playus.communityservice.domain.common.userInfo;

import com.playus.communityservice.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadService {

    private final UserQueryClient userQueryClient;

    public UserInfoResponse getUserInfo(Long userId) {
        List<PartyApplicantsInfoFeignResponse> users = userQueryClient.getUserInfos(List.of(userId));

        if (users.isEmpty()) {
            throw new EntityNotFoundException("사용자");
        }

        PartyApplicantsInfoFeignResponse info = users.get(0);

        return new UserInfoResponse(info.userId(), info.name(), info.thumbnailUrl());
    }
}

