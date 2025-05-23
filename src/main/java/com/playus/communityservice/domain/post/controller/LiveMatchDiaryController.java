package com.playus.communityservice.domain.post.controller;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.service.PostService;
import com.playus.communityservice.global.jwt.JwtUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/live-match-diary")
@RequiredArgsConstructor
public class LiveMatchDiaryController {

    private final PostService postService;

    @PostMapping("/{tag}")
    public ResponseEntity<PostCreateResponse> createPost(@PathVariable TeamTag tag,
                                                         @Valid @RequestBody PostCreateRequest request,
                                                         @AuthenticationPrincipal JwtUser user) {
        PostCreateResponse response = postService.createPost(request, user, tag);
        URI location = URI.create("/live-match-diary/" + response.postId());
        return ResponseEntity.created(location).body(response);
    }
}
