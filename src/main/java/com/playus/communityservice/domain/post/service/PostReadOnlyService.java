package com.playus.communityservice.domain.post.service;

import com.playus.communityservice.domain.comment.document.CommentDocument;
import com.playus.communityservice.domain.comment.document.CommentGroupDocument;
import com.playus.communityservice.domain.comment.repository.read.CommentGroupReadOnlyRepository;
import com.playus.communityservice.domain.comment.repository.read.CommentReadOnlyRepository;
import com.playus.communityservice.domain.post.document.PostDocument;
import com.playus.communityservice.domain.post.dto.diary_view.DiaryGetResponse;
import com.playus.communityservice.domain.post.dto.diary_view.DiaryListResponse;
import com.playus.communityservice.domain.post.dto.post_view.PostGetResponse;
import com.playus.communityservice.domain.post.dto.post_view.PostListResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.domain.post.repository.read.PostReadOnlyRepository;
import com.playus.communityservice.domain.common.userInfo.UserInfo;
import com.playus.communityservice.domain.common.userInfo.UserReadService;
import com.playus.communityservice.global.exception.EntityNotFoundException;
import com.playus.communityservice.domain.common.security.JwtUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReadOnlyService {

    private final PostReadOnlyRepository postRepository;
    private final CommentReadOnlyRepository commentRepository;
    private final CommentGroupReadOnlyRepository commentGroupRepository;
    private final UserReadService userReadService;

    public PostGetResponse getPost(Long postId, JwtUser user) {
        return buildPostGetResponse(postId, null);
    }

    public PostGetResponse getPostById(Long writerId, Long postId, JwtUser user) {
        return buildPostGetResponse(postId, writerId);
    }

    private PostGetResponse buildPostGetResponse(Long postId, Long writerId) {
        PostDocument post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글"));

        if (!post.isActivated()) {
            throw new EntityNotFoundException("게시글");
        }

        if (writerId != null && !post.getWriterId().equals(writerId)) {
            throw new EntityNotFoundException("작성자와 게시글이 일치하지 않습니다.");
        }

        List<CommentGroupDocument> groups = commentGroupRepository.findAllByPostId(postId);
        List<Long> groupIds = groups.stream()
                .map(CommentGroupDocument::getId)
                .toList();

        List<CommentDocument> allComments = commentRepository.findAllByCommentGroupIdIn(groupIds);

        List<PostGetResponse.CommentDto> comments = allComments.stream()
                .filter(c -> c.getCommentOrder() == 1L && c.isActivated())
                .map(comment -> {
                    UserInfo userInfo = UserInfo.from(userReadService, comment.getUserId());

                    List<PostGetResponse.ReCommentDto> reComments = allComments.stream()
                            .filter(reply -> Objects.equals(reply.getCommentGroupId(), comment.getCommentGroupId())
                                    && reply.getCommentOrder() > 1L
                                    && reply.isActivated())
                            .map(reply -> {
                                UserInfo replyUser = UserInfo.from(userReadService, reply.getUserId());
                                return new PostGetResponse.ReCommentDto(
                                        reply.getId(),
                                        reply.getCommentGroupId(),
                                        replyUser.nickname(),
                                        replyUser.profileImage(),
                                        reply.getContent()
                                );
                            })
                            .toList();

                    return new PostGetResponse.CommentDto(
                            comment.getId(),
                            comment.getCommentGroupId(),
                            userInfo.nickname(),
                            userInfo.profileImage(),
                            comment.getContent(),
                            reComments
                    );
                })
                .toList();

        UserInfo writerInfo = UserInfo.from(userReadService, post.getWriterId());

        return new PostGetResponse(
                post.getId(),
                post.getTag(),
                post.getTitle(),
                post.getTwpDate(),
                writerInfo.nickname(),
                writerInfo.profileImage(),
                post.getImageUrl(),
                post.getDescription(),
                comments
        );
    }


    public DiaryGetResponse getMyDiary(Long postId, JwtUser user) {
        PostDocument post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("직관일지"));

        if (!post.isActivated() || !post.isSecret() || !post.getWriterId().equals(user.getId())) {
            throw new EntityNotFoundException("직관일지");
        }

        return new DiaryGetResponse(
                post.getTitle(),
                post.getTwpDate(),
                post.getImageUrl(),
                post.getDescription()
        );
    }


    public List<PostListResponse> getPostsByTeam(TeamTag tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<PostDocument> posts = postRepository.findByTagWithPaging(tag, pageable);

        return posts.stream()
                .map(post -> {
                    UserInfo writerInfo = UserInfo.from(userReadService, post.getWriterId());
                    return new PostListResponse(
                            post.getId(),
                            post.getTitle(),
                            post.getCreatedAt().toLocalDate(),
                            writerInfo.nickname(),
                            post.getImageUrl()
                    );
                })
                .toList();
    }


    public List<DiaryListResponse> getMyDiaries(JwtUser user) {
        List<PostDocument> posts = postRepository.findAllByWriterIdAndIsSecretTrue(user.getId());

        return posts.stream()
                .filter(post -> post.isActivated() && post.isSecret())
                .map(post -> new DiaryListResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getImageUrl(),
                        post.getTwpDate(),
                        post.getTag()
                ))
                .toList();
    }

    public List<PostListResponse> getPostsByWriter(Long writerId) {
        if (writerId == null || writerId <= 0) {
            throw new InvalidParameterException("유효하지 않은 작성자 ID입니다.");
        }

        List<PostDocument> posts = postRepository.findAllByWriterIdAndIsSecretFalse(writerId);

        return posts.stream()
                .filter(PostDocument::isActivated)
                .map(post -> {
                    UserInfo writerInfo = UserInfo.from(userReadService, post.getWriterId());
                    return new PostListResponse(
                            post.getId(),
                            post.getTitle(),
                            post.getCreatedAt().toLocalDate(),
                            writerInfo.nickname(),
                            post.getImageUrl()
                    );
                })
                .toList();
    }

}
