package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.comment.entity.CommentGroup;
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

    private final CommentGroupRepository commentGroupRepository;
    private final CommentRepository commentRepository;

    public PostCreateResponse createPost(PostCreateRequest request, JwtUser user, TeamTag tag) {
        if (request.isSecret()) {
            return createDiary(request, user, tag);
        } else {
            return createGeneralPost(request, user, tag);
        }
    }

    private PostCreateResponse createGeneralPost(PostCreateRequest request, JwtUser user, TeamTag tag) {
        Post post = Post.create(
                user.getId(),
                request.title(),
                request.content(),
                tag,
                request.twpDate(),
                request.image(),
                false
        );
        postRepository.save(post);
        return PostCreateResponse.of(post.getId(), "게시물 생성이 완료되었습니다.");
    }

    private PostCreateResponse createDiary(PostCreateRequest request, JwtUser user, TeamTag tag) {
        if (request.twpDate() == null) {
            throw new IllegalArgumentException("직관일지는 twpDate가 필수입니다.");
        }

        Post diary = Post.create(
                user.getId(),
                request.title(),
                request.content(),
                tag,
                request.twpDate(),
                request.image(),
                true
        );
        postRepository.save(diary);
        return PostCreateResponse.of(diary.getId(), "직관일지 생성이 완료되었습니다.");
    }

    public PostUpdateResponse updatePost(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        if (request.isSecret()) {
            return updateDiary(request, user, tag);
        } else {
            return updateGeneralPost(request, user, tag);
        }
    }


    public PostUpdateResponse updateGeneralPost(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException("게시글");
        }

        String currentImage = post.getImageUrl();
        if (currentImage != null && !currentImage.isEmpty() &&
        !currentImage.equals(request.image())) {
            String fileName = currentImage.substring(currentImage.lastIndexOf("/")+1);
            s3Service.deleteImage(fileName);
        }

        post.updateAll(request.title(), request.content(), tag, false, request.twpDate(), request.image());
        return PostUpdateResponse.of(true, "게시물이 수정되었습니다.");
    }

    public PostUpdateResponse updateDiary(PostUpdateRequest request, JwtUser user, TeamTag tag) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("직관일지"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException("직관일지");
        }

        if (request.twpDate() == null) {
            throw new IllegalArgumentException("직관일지는 twpDate가 필수입니다.");
        }

        String currentImage = post.getImageUrl();
        if (currentImage != null && !currentImage.isEmpty() &&
                !currentImage.equals(request.image())) {
            String fileName = currentImage.substring(currentImage.lastIndexOf("/") + 1);
            s3Service.deleteImage(fileName);
        }

        post.updateAll(
                request.title(),
                request.content(),
                tag,
                true, // isSecret 유지
                request.twpDate(),
                request.image()
        );

        return PostUpdateResponse.of(true, "직관일지가 수정되었습니다.");
    }

    public PostDeleteResponse deletePost(PostDeleteRequest request, JwtUser user) {
        // DB 조회 후 isSecret 판단
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (post.isSecret()) {
            return deleteDiary(request, user);
        } else {
            return deleteGeneralPost(request, user);
        }
    }

    public PostDeleteResponse deleteGeneralPost(PostDeleteRequest request, JwtUser user) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException("게시글");
        }

        List<CommentGroup> commentGroups = commentGroupRepository.findAllByPost(post);

        for (CommentGroup group : commentGroups) {

            List<Comment> comments = commentRepository.findAllByCommentGroup(group);
            comments.forEach(Comment::delete);

            commentGroupRepository.delete(group);
        }
        post.delete();

        return PostDeleteResponse.of(true, "게시물이 삭제되었습니다.");
    }

    public PostDeleteResponse deleteDiary(PostDeleteRequest request, JwtUser user) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("직관일지"));

        if (!post.getWriterId().equals(user.getId())) {
            throw new ForbiddenAccessException("직관일지");
        }

        List<CommentGroup> commentGroups = commentGroupRepository.findAllByPost(post);
        for (CommentGroup group : commentGroups) {
            List<Comment> comments = commentRepository.findAllByCommentGroup(group);
            comments.forEach(Comment::delete);
            commentGroupRepository.delete(group);
        }

        post.delete();

        return PostDeleteResponse.of(true, "직관일지가 삭제되었습니다.");
    }


    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
        return new PresignedUrlForSaveImageResponse(s3Service.generatePresignedUrl(request.imageFileName()));
    }
}

