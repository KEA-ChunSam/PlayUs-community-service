package com.playus.communityservice.domain.post.document;

import com.playus.communityservice.domain.post.enums.Tag;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(value = "post")
public class PostDocument {

    @Id
    private Long id;

    @NotNull
    @Field(name = "writer_id")
    private Long writerId;

    @NotNull
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    private Tag tag;

    @NotNull
    private String description;

    @NotNull
    private boolean activated;

    @NotNull
    @Field(name = "is_secret")
    private boolean isSecret;


    @Builder
    private PostDocument(Long id, String title, String description, Tag tag, Long writerId, boolean activated, boolean isSecret) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.writerId = writerId;
        this.activated = activated;
        this.isSecret = isSecret;
    }

    public static PostDocument createForOnlyTest(Long id, Long writerId, String title, String description, Tag tag) {
        return PostDocument.builder()
                .id(id)
                .writerId(writerId)
                .title(title)
                .description(description)
                .tag(tag)
                .activated(true)
                .isSecret(false)
                .build();
    }
}
