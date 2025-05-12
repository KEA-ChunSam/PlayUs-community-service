package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.file.entity.File;
import com.playus.communityservice.domain.file.repository.write.FileRepository;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.entity.PostImage;
import com.playus.communityservice.domain.post.enums.Tag;
import com.playus.communityservice.domain.post.repository.write.PostImageRepository;
import com.playus.communityservice.domain.post.repository.write.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public void createPost(PostCreateRequest request) {
        // 기본값 지정
        Long writerId = 1L; // 나중에 인증 연동 시 대체
        Tag defaultTag = Tag.LG_TWINS; // 기본 태그 (클라이언트가 안 보내니까 서버에서 결정)

        // Post 생성 및 저장
        Post post = Post.create(
                writerId,
                request.title(),
                request.message(),
                defaultTag
        );
        post = postRepository.save(post);

        // image 필드가 있으면 PostImage로 연결
        if (request.image() != null && !request.image().isBlank()) {
            try {
                Long fileId = Long.parseLong(request.image());
                File file = fileRepository.findById(fileId)
                        .orElseThrow(() -> new IllegalArgumentException("File not found with id = " + fileId));
                PostImage postImage = PostImage.create(post, file);
                postImageRepository.save(postImage);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Image field must be a valid numeric string (file ID)");
            }
        }
    }

    @Transactional
    public void deletePost(PostDeleteRequest request) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id = " + request.postId()));

        if (!post.isActivated()) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }

        post.delete();
    }

    @Transactional
    public PostUpdateResponse updatePost(PostUpdateRequest request) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id = " + request.postId()));

        if (!post.isActivated()) {
            throw new IllegalStateException("이미 삭제된 게시글은 수정할 수 없습니다.");
        }

        post.updateAll(
                request.title(),
                request.content(),
                Tag.LG_TWINS, // 임시로 태그는 고정 처리
                false         // isSecret도 false 고정 처리
        );

        return PostUpdateResponse.ok();
    }

}
