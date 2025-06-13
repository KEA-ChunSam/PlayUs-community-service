package com.playus.communityservice.domain.post.repository.read;

import com.playus.communityservice.domain.post.document.PostDocument;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostReadOnlyRepository extends MongoRepository<PostDocument, Long> {
    @Query("{ 'tag': ?0, 'isSecret': false, 'activated': true }")
    List<PostDocument> findByTagWithPaging(TeamTag tag, Pageable pageable);
    List<PostDocument> findAllByWriterIdAndIsSecretTrue(Long writerId);
    List<PostDocument> findAllByWriterIdAndIsSecretFalse(Long writerId);
}
