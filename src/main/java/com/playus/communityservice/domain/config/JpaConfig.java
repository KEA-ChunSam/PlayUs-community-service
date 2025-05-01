package com.playus.communityservice.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.playus.communityservice.domain.comment.repository.write",
                "com.playus.communityservice.domain.file.repository.write",
                "com.playus.communityservice.domain.post.repository.write"
        }
)
@EntityScan(
        basePackages = {
                "com.playus.communityservice.domain.comment.entity",
                "com.playus.communityservice.domain.file.entity",
                "com.playus.communityservice.domain.post.entity"
        }
)
public class JpaConfig {
}
