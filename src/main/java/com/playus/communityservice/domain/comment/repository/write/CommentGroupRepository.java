package com.playus.communityservice.domain.comment.repository.write;

import com.playus.communityservice.domain.comment.entity.CommentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentGroupRepository extends JpaRepository<CommentGroup, Long> {
}
