package com.playus.communityservice.domain.post.repository.write;

import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.enums.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTag(TeamTag tag);
    Page<Post> findAllByTag(TeamTag tag, Pageable pageable);
}
