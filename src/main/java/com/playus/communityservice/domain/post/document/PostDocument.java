package com.playus.communityservice.domain.post.document;

import com.playus.communityservice.domain.post.enums.TeamTag;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDate;

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
    private TeamTag tag;

    @NotNull
    private String description;

    @NotNull
    private boolean activated;

    @NotNull
    @Field(name = "is_secret")
    private boolean isSecret;

    @Field(name = "jwp_date")
    private LocalDate jwpDate;

    @Field(name = "image_url")
    private String imageUrl;

    @NotNull
    private int view;


    @Builder
    private PostDocument(Long id, String title, String description, TeamTag tag, Long writerId, boolean activated, boolean isSecret, String imageUrl, LocalDate jwpDate, int view) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.writerId = writerId;
        this.activated = activated;
        this.isSecret = isSecret;
        this.jwpDate = jwpDate;
        this.imageUrl = imageUrl;
        this.view = view;
    }

    public static PostDocument createForOnlyTest(Long id, Long writerId, String title, String description, TeamTag tag, String imageUrl, LocalDate jwpDate, int view) {
        return PostDocument.builder()
                .id(id)
                .writerId(writerId)
                .title(title)
                .description(description)
                .tag(tag)
                .activated(true)
                .isSecret(false)
                .jwpDate(jwpDate)
                .imageUrl(imageUrl)
                .view(view)
                .build();
    }

    public void increaseView() { this.view++; }
}
