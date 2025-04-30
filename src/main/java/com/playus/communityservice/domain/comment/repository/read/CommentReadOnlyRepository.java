package com.playus.communityservice.domain.comment.repository.read;

import com.playus.communityservice.domain.comment.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentReadOnlyRepository extends MongoRepository<CommentDocument, Long> {
}
