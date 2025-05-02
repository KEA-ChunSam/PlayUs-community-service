package com.playus.communityservice.domain.comment.repository.write;

import com.playus.communityservice.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
