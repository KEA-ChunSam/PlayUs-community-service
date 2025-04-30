package com.playus.communityservice.domain.config.mongo;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = {
                "com.playus.communityservice.domain.comment.repository.read",
                "com.playus.communityservice.domain.file.repository.read",
                "com.playus.communityservice.domain.post.repository.read"
        },
        mongoTemplateRef = "readMongoTemplate"

)
public class ReadMongoConfig {
}
