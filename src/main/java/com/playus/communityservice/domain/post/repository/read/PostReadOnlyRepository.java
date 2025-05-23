package com.playus.communityservice.domain.post.repository.read;

import com.playus.communityservice.domain.post.document.PostDocument;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostReadOnlyRepository extends MongoRepository<PostDocument, Long> {
    Page<PostDocument> findAllByTag(TeamTag tag, Pageable pageable);
    Page<PostDocument> findAllByWriterIdAndTagAndIsSecretTrue(Long writerId, TeamTag tag, Pageable pageable);

}
