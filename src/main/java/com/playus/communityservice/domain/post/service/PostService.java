package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.comment.repository.write.CommentGroupRepository;
import com.playus.communityservice.domain.comment.repository.write.CommentRepository;
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
import com.playus.communityservice.domain.common.security.JwtUser;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.global.exception.ForbiddenAccessException;
import com.playus.communityservice.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    private final CommentGroupRepository commentGroupRepository;
    private final CommentRepository commentRepository;

    public PostCreateResponse createGeneralPost(PostCreateRequest request, JwtUser user, TeamTag tag) {
        if (request.isSecret()) {
            throw new IllegalArgumentException("일반 게시글 생성 API는 isSecret=false 여야 합니다.");
        }
        return createPostInternal(request, user, tag, false);
    }

    public PostCreateResponse createDiaryPost(PostCreateRequest request, JwtUser user, TeamTag tag) {
        if (!request.isSecret()) {
            throw new IllegalArgumentException("직관일지 생성 API는 isSecret=true 여야 합니다.");
        }
        if (request.twpDate() == null) {
            throw new IllegalArgumentException("직관일지는 twpDate가 필수입니다.");
        }
        return createPostInternal(request, user, tag, true);
    }

    private PostCreateResponse createPostInternal(PostCreateRequest request, JwtUser user, TeamTag tag, boolean isSecret) {
        Post post = Post.create(
                user.getId(),
                request.title(),
                request.content(),
                tag,
                request.twpDate(),
                request.image(),
                isSecret
        );
        postRepository.save(post);

        String message = isSecret ? "직관일지 생성이 완료되었습니다." : "게시물 생성이 완료되었습니다.";
        return PostCreateResponse.of(post.getId(), message);
    }


    // PostController에서 호출
    public PostUpdateResponse updateGeneralPost(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        return updatePostInternal(request, user, tag, false);
    }

    // LiveMatchDiaryController에서 호출
    public PostUpdateResponse updateDiaryPost(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        return updatePostInternal(request, user, tag, true);
    }

    // 공통 로직 메서드
    private PostUpdateResponse updatePostInternal(PostUpdateRequest request, JwtUser user, TeamTag tag, boolean isSecret) {

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException(isSecret ? "직관일지" : "게시글"));


        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException(isSecret ? "직관일지" : "게시글");
        }

        if (isSecret && request.twpDate() == null) {
            throw new IllegalArgumentException("직관일지는 twpDate가 필수입니다.");
        }

        String currentImage = post.getImageUrl();
        if (currentImage != null && !currentImage.isEmpty() && !currentImage.equals(request.image())) {
            String fileName = currentImage.substring(currentImage.lastIndexOf("/") + 1);
            s3Service.deleteImage(fileName);
        }

        post.updateAll(
                request.title(),
                request.content(),
                tag,
                isSecret,
                request.twpDate(),
                request.image()
        );

        return PostUpdateResponse.of(true, (isSecret ? "직관일지" : "게시글") + "이(가) 수정되었습니다.");
    }


    public PostDeleteResponse deletePost(PostDeleteRequest request, JwtUser user) {

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("해당글"));

        String typeName = post.isSecret() ? "직관일지" : "게시글";

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException(typeName);
        }

        commentGroupRepository.findAllByPost(post).forEach(group -> {
            commentRepository.findAllByCommentGroup(group).forEach(Comment::delete);
            commentGroupRepository.delete(group);
        });

        post.delete();

        return PostDeleteResponse.of(true, typeName + "이(가) 삭제되었습니다.");
    }


    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
        return new PresignedUrlForSaveImageResponse(s3Service.generatePresignedUrl(request.imageFileName()));
    }

    public void increaseView(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        post.increaseView(); // MySQL 기반 Entity의 view 필드 증가
    }
}

