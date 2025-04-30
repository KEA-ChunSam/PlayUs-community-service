package com.playus.communityservice.domain.comment.document;

import com.playus.communityservice.domain.post.entity.Post;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(value = "comment_group")
public class CommentGroupDocument {

    @Id
    private Long id;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "post_id")
    private Post post;

    @Builder
    private CommentGroupDocument(Long id, Post post) {
        this.id = id;
        this.post = post;
    }

    public static CommentGroupDocument createForOnlyTest(Long id, Post post) {
        return CommentGroupDocument.builder()
                .id(id)
                .post(post)
                .build();
    }
}
