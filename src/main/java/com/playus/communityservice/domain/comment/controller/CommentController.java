package com.playus.communityservice.domain.comment.controller;

import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateRequest;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateResponse;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteRequest;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteResponse;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateRequest;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateResponse;
import com.playus.communityservice.domain.comment.service.CommentService;
import com.playus.communityservice.domain.comment.specification.CommentControllerSpecification;
import com.playus.communityservice.domain.common.security.JwtUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController implements CommentControllerSpecification {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(@Valid @RequestBody CommentCreateRequest request,
                                                               @AuthenticationPrincipal JwtUser user) {
        CommentCreateResponse response = commentService.createComment(request, user);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(@Valid @RequestBody CommentUpdateRequest request,
                                                               @PathVariable Long commentId,
                                                               @AuthenticationPrincipal JwtUser user) {
        CommentUpdateResponse response = commentService.updateComment(request, user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDeleteResponse> deleteComment(@Valid @RequestBody CommentDeleteRequest request,
                                                               @PathVariable Long commentId,
                                                               @AuthenticationPrincipal JwtUser user) {
        CommentDeleteResponse response = commentService.deleteComment(request, user);
        return ResponseEntity.ok(response);
    }
}