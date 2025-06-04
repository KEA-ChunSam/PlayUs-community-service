package com.playus.communityservice.domain.comment.repository.read;

import com.playus.communityservice.domain.comment.document.CommentGroupDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CommentGroupReadOnlyRepository extends MongoRepository<CommentGroupDocument, Long> {

    List<CommentGroupDocument> findAllByPostId(Long postId);
}
