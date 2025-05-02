package com.playus.communityservice.domain.post.repository.read;

import com.playus.communityservice.domain.post.document.PostDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostReadOnlyRepository extends MongoRepository<PostDocument, Long> {
}
