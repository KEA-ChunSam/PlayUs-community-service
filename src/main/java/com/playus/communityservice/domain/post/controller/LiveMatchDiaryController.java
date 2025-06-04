package com.playus.communityservice.domain.post.controller;

import com.playus.communityservice.domain.post.dto.diary_view.DiaryGetResponse;
import com.playus.communityservice.domain.post.dto.diary_view.DiaryListResponse;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.service.PostReadOnlyService;
import com.playus.communityservice.domain.post.service.PostService;
import com.playus.communityservice.domain.post.specification.LiveMatchDiaryControllerSpecification;
import com.playus.communityservice.domain.common.security.JwtUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/live-match-diary")
@RequiredArgsConstructor
public class LiveMatchDiaryController implements LiveMatchDiaryControllerSpecification {

    private final PostService postService;
    private final PostReadOnlyService postReadOnlyService;

    @PostMapping("/{tag}")
    public ResponseEntity<PostCreateResponse> createPost(@PathVariable TeamTag tag,
                                                         @Valid @RequestBody PostCreateRequest request,
                                                         @AuthenticationPrincipal JwtUser user) {
        PostCreateResponse response = postService.createDiaryPost(request, user, tag);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{tag}/{postId}")
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
        PostUpdateResponse response = postService.updateDiaryPost(updatedRequest, user, tag);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDeleteResponse> deletePost(@PathVariable Long postId,
                                                         @AuthenticationPrincipal JwtUser user) {
        PostDeleteRequest request = new PostDeleteRequest(postId);
        PostDeleteResponse response = postService.deletePost(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tag}/{postId}")
    public ResponseEntity<DiaryGetResponse> getMyDiary(@PathVariable TeamTag tag,
                                                       @PathVariable Long postId,
                                                       @AuthenticationPrincipal JwtUser user) {
        DiaryGetResponse response = postReadOnlyService.getMyDiary(postId, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<DiaryListResponse>> getMyDiaries(
            @AuthenticationPrincipal JwtUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        List<DiaryListResponse> response = postReadOnlyService.getMyDiaries(user, page, size);
        return ResponseEntity.ok(response);
    }
}
