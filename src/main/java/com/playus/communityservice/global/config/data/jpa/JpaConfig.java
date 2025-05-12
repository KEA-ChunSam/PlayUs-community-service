package com.playus.communityservice.global.config.data.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.playus.communityservice.domain.comment.repository.write",
                "com.playus.communityservice.domain.file.repository.write",
                "com.playus.communityservice.domain.post.repository.write"
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "com\\.playus\\.communityservice\\.domain\\.comment\\.repository\\.read\\..*"
                ),
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "com\\.playus\\.communityservice\\.domain\\.file\\.repository\\.read\\..*"
                ),
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "com\\.playus\\.communityservice\\.domain\\.post\\.repository\\.read\\..*"
                )
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
