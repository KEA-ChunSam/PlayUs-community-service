package com.playus.communityservice.domain.post.controller;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.service.PostService;
import com.playus.communityservice.domain.post.specification.PostControllerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController implements PostControllerSpecification {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody @Valid PostCreateRequest request) {
        postService.createPost(request);
        return ResponseEntity.ok("{\"ok\"}");
    }

    @PatchMapping
    public ResponseEntity<String> deletePost(@RequestBody @Valid PostDeleteRequest request) {
        postService.deletePost(request);
        return ResponseEntity.ok("{}"); // 명세서에 맞게 빈 JSON 반환
    }

    @PutMapping
    public ResponseEntity<PostUpdateResponse> updatePost(@RequestBody @Valid PostUpdateRequest request) {
        PostUpdateResponse response = postService.updatePost(request);
        return ResponseEntity.ok(response);
    }
}
