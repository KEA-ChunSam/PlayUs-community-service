package com.playus.communityservice.domain.config.mongo;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.playus.communityservice.domain.repository.read",
        mongoTemplateRef = "readMongoTemplate"
)
public class ReadMongoConfig {
}
