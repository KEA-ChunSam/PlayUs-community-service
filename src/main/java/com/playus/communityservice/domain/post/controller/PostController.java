package com.playus.communityservice.domain.post.controller;

import com.playus.communityservice.domain.post.dto.post_view.PostGetResponse;
import com.playus.communityservice.domain.post.dto.post_view.PostListResponse;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.communityservice.domain.post.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.service.PostReadOnlyService;
import com.playus.communityservice.domain.post.service.PostService;
import com.playus.communityservice.domain.post.specification.PostControllerSpecification;
import com.playus.communityservice.domain.common.security.JwtUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController implements PostControllerSpecification {

    private final PostService postService;
    private final PostReadOnlyService postReadOnlyService;

    @PostMapping("/{tag}")
    public ResponseEntity<PostCreateResponse> createPost(@PathVariable TeamTag tag,
                                                     @Valid @RequestBody PostCreateRequest request,
                                                     @AuthenticationPrincipal JwtUser user) {
        PostCreateResponse response = postService.createGeneralPost(request, user, tag);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{tag}/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable TeamTag tag,
                                                         @PathVariable Long postId,
                                                         @Valid @RequestBody PostUpdateRequest request,
                                                         @AuthenticationPrincipal JwtUser user) {
        PostUpdateRequest updatedRequest = new PostUpdateRequest(
                postId,
                request.title(),
                request.image(),
                request.content(),
                request.twpDate(),
                request.isSecret()
        );
        PostUpdateResponse response = postService.updateGeneralPost(updatedRequest, user, tag);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<PostDeleteResponse> deletePost(@PathVariable Long postId,
                                                     @AuthenticationPrincipal JwtUser user) {
        PostDeleteRequest request = new PostDeleteRequest(postId);
        PostDeleteResponse response = postService.deletePost(request, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/presigned-url")
    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @RequestBody PresignedUrlForSaveImageRequest request) {
        return postService.generatePresignedUrlForSaveImage(request);
    }

    @GetMapping("/{tag}/{postId}")
    public ResponseEntity<PostGetResponse> getPost(@PathVariable TeamTag tag,
                                                   @PathVariable Long postId,
                                                   @AuthenticationPrincipal JwtUser user) {
        PostGetResponse response = postReadOnlyService.getPost(postId, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{teamName}")
    public ResponseEntity<List<PostListResponse>> getPostsByTeam(
            @PathVariable("teamName") TeamTag teamName,
            @AuthenticationPrincipal JwtUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostListResponse> response = postReadOnlyService.getPostsByTeam(teamName, page, size);
        return ResponseEntity.ok(response);
    }

}
