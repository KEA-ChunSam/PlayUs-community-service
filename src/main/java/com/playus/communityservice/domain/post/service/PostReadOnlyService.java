package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.comment.repository.read.CommentReadOnlyRepository;
import com.playus.communityservice.domain.post.document.PostDocument;
import com.playus.communityservice.domain.post.dto.PostGetResponse;
import com.playus.communityservice.domain.post.dto.PostListResponse;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.repository.read.PostReadOnlyRepository;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.global.jwt.JwtUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostReadOnlyService {

    private final PostReadOnlyRepository postRepository;
    private final CommentReadOnlyRepository commentRepository;

    public PostGetResponse getPost(Long postId, JwtUser user) {
        PostDocument post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.isActivated()) {
            throw new EntityNotFoundException("게시글");
        }

        post.increaseView();

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
