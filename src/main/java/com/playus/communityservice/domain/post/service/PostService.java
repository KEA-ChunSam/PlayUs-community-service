package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.comment.entity.CommentGroup;
import com.playus.communityservice.domain.comment.repository.write.CommentGroupRepository;
import com.playus.communityservice.domain.comment.repository.write.CommentRepository;
import com.playus.communityservice.domain.post.dto.PostGetResponse;
import com.playus.communityservice.domain.post.dto.PostListResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        String currentImage = post.getImageUrl();
        if (currentImage != null && !currentImage.isEmpty() &&
        !currentImage.equals(request.image())) {
            String fileName = currentImage.substring(currentImage.lastIndexOf("/")+1);
            s3Service.deleteImage(fileName);
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

        List<CommentGroup> commentGroups = commentGroupRepository.findAllByPost(post);

        for (CommentGroup group : commentGroups) {

            List<Comment> comments = commentRepository.findAllByCommentGroup(group);
            comments.forEach(Comment::delete);

            commentGroupRepository.delete(group);
        }
        post.delete();

        return PostDeleteResponse.of(true, "게시물이 삭제되었습니다.");
    }

    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
        return new PresignedUrlForSaveImageResponse(s3Service.generatePresignedUrl(request.imageFileName()));
    }

    public PostGetResponse getPost(Long postId, JwtUser user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.isActivated()) {
            throw new EntityNotFoundException("게시글");
        }

        List<Comment> allComments = commentRepository.findAllByCommentGroup_Post(post);

        List<PostGetResponse.CommentDto> comments = allComments.stream()
                .filter(c -> c.getCommentOrder() == 1L)
                .map(comment -> {
                    List<PostGetResponse.ReCommentDto> reComments = allComments.stream()
                            .filter(reply -> reply.getCommentGroup().equals(comment.getCommentGroup()) && reply.getCommentOrder() > 1L)
                            .map(reply -> new PostGetResponse.ReCommentDto(
                                    reply.getId(),
                                    getNickname(reply.getUserId()),
                                    getProfileImage(reply.getUserId()),
                                    isExpert(reply.getUserId()),
                                    reply.getContent()
                            ))
                            .toList();

                    return new PostGetResponse.CommentDto(
                            comment.getId(),
                            getNickname(comment.getUserId()),
                            getProfileImage(comment.getUserId()),
                            isExpert(comment.getUserId()),
                            comment.getContent(),
                            reComments
                    );
                })
                .toList();

        return new PostGetResponse(
                post.getId(),
                post.getTitle(),
                post.getJwpDate(),
                getNickname(post.getWriterId()),
                getProfileImage(post.getWriterId()),
                isExpert(post.getWriterId()),
                post.getImageUrl(),
                post.getDescription(),
                comments
        );
    }

    public List<PostListResponse> getPostsByTeam(TeamTag tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAllByTag(tag, pageable);
        List<Post> posts = postPage.getContent();

        return posts.stream()
                .map(post -> new PostListResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getJwpDate(),
                        getNickname(post.getWriterId()),
                        post.getImageUrl()
                ))
                .toList();
    }

    private String getNickname(Long userId) {
        return "User#" + userId; // TODO: 유저 서비스 연동
    }

    private String getProfileImage(Long userId) {
        return "https://example.com/profile/" + userId + ".png"; // TODO: 유저 서비스 연동
    }

    private boolean isExpert(Long userId) {
        return false; // TODO: 유저 서비스 연동
    }
}

