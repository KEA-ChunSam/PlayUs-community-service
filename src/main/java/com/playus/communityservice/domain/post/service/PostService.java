package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.communityservice.domain.post.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.repository.write.PostRepository;
import com.playus.communityservice.global.jwt.JwtUser;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.global.exception.ForbiddenAccessException;
import com.playus.communityservice.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    private final S3Service s3Service;


    public PostCreateResponse createPost(PostCreateRequest request, JwtUser user, TeamTag tag) {
        Post post = Post.create(user.getId(), request.title(), request.content(), tag, request.jwpDate(), request.image());
        postRepository.save(post);

        return PostCreateResponse.of(post.getId(), "게시물 생성이 완료되었습니다.");
    }


    public PostUpdateResponse updatePost(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException("게시글");
        }

        post.updateAll(request.title(), request.content(), tag, false, request.jwpDate(), request.image());
        return PostUpdateResponse.of(true, "게시물이 수정되었습니다.");
    }


    public PostDeleteResponse deletePost(PostDeleteRequest request, JwtUser user) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException("게시글");
        }

        post.delete();
        return PostDeleteResponse.of(true, "게시물이 삭제되었습니다.");
    }

    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
        return new PresignedUrlForSaveImageResponse(s3Service.generatePresignedUrl(request.imageFileName()));
    }

}
