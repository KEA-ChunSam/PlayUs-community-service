package com.playus.communityservice.domain.post.repository.write;

import com.playus.communityservice.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
