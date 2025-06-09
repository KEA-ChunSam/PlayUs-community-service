package com.playus.communityservice.domain.comment.repository.write;

import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.comment.entity.CommentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCommentGroup(CommentGroup commentGroup);
    boolean existsByCommentGroupAndActivatedTrue(CommentGroup commentGroup);
    Optional<Comment> findByCommentOrderAndCommentGroup(long commentOrder, CommentGroup commentGroup);
}
