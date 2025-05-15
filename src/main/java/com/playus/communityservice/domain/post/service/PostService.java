package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.repository.write.PostRepository;
import com.playus.communityservice.global.config.jwt.JwtUser;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.global.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, JwtUser user, TeamTag tag) {
        Post post = Post.create(user.getId(), request.title(), request.content(), tag, request.jwpDate());
        postRepository.save(post);

        return PostCreateResponse.of(post.getId(), "게시물 생성이 완료되었습니다.");
    }

    @Transactional
    public PostUpdateResponse updatePost(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new UnauthorizedAccessException("게시글");
        }

        post.updateAll(request.title(), request.content(), TeamTag.DOOSAN_BEARS, false, request.jwpDate());
        return PostUpdateResponse.fo(true, "게시물이 수정되었습니다.");
    }

    @Transactional
    public PostDeleteResponse deletePost(PostDeleteRequest request, JwtUser user) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new UnauthorizedAccessException("게시글");
        }

        post.delete();
        return PostDeleteResponse.of(true, "게시물이 삭제되었습니다.");
    }
}
