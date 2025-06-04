package com.playus.communityservice.domain.comment.document;

import com.playus.communityservice.domain.post.document.PostDocument;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(value = "comment_group")
public class CommentGroupDocument {

    @Id
    private Long id;

    @NotNull
    @Field(name = "post_id")
    private Long postId;

    @Builder
    private CommentGroupDocument(Long id, Long postId) {
        this.id = id;
        this.postId = postId;
    }

    public static CommentGroupDocument createForOnlyTest(Long id, Long postId) {
        return CommentGroupDocument.builder()
                .id(id)
                .postId(postId)
                .build();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentGroupDocument that)) return false;
        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
