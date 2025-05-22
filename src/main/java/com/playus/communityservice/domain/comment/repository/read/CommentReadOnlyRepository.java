package com.playus.communityservice.domain.comment.repository.read;

import com.playus.communityservice.domain.comment.document.CommentDocument;
import com.playus.communityservice.domain.post.document.PostDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentReadOnlyRepository extends MongoRepository<CommentDocument, Long> {
    List<CommentDocument> findAllByCommentGroup_Post(PostDocument post);
}
