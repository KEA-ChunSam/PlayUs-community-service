package com.playus.communityservice.domain.post.repository.write;

import com.playus.communityservice.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
