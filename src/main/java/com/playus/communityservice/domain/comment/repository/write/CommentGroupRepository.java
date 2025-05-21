package com.playus.communityservice.domain.comment.repository.write;

import com.playus.communityservice.domain.comment.entity.CommentGroup;
import com.playus.communityservice.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentGroupRepository extends JpaRepository<CommentGroup, Long> {
    List<CommentGroup> findAllByPost(Post post);
}
