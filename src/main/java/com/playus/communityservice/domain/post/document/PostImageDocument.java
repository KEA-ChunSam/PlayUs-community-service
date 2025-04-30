package com.playus.communityservice.domain.post.document;

import com.playus.communityservice.domain.file.document.FileDocument;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(value = "post_image")
public class PostImageDocument {

    @Id
    private Long id;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "post_id")
    private PostDocument post;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "file_id")
    private FileDocument file;


    @Builder
    private PostImageDocument(Long id, PostDocument post, FileDocument file) {
        this.id = id;
        this.post = post;
        this.file = file;
    }

    public static PostImageDocument createForOnlyTest(Long id, PostDocument post, FileDocument file) {
        return PostImageDocument.builder()
                .id(id)
                .post(post)
                .file(file)
                .build();
    }
}
