package com.playus.communityservice.domain.post.document;

import com.playus.communityservice.domain.file.entity.File;
import com.playus.communityservice.domain.post.entity.Post;
import com.playus.communityservice.domain.post.entity.PostImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(value = "post_image")
public class PostImageDocument {

    @Id
    private Long id;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "post_id")
    private Post post;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "file_id")
    private File file;


    @Builder
    private PostImageDocument(Long id, Post post, File file) {
        this.id = id;
        this.post = post;
        this.file = file;
    }

    public static PostImageDocument createForOnlyTest(Long id, Post post, File file) {
        return PostImageDocument.builder()
                .id(id)
                .post(post)
                .file(file)
                .build();
    }
}
