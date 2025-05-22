package com.playus.communityservice.domain.post.entity;

import com.playus.communityservice.domain.common.BaseTimeEntity;
import com.playus.communityservice.domain.post.enums.TeamTag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "writer_id", nullable = false)
    private Long writerId;

    @Column(length = 100, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamTag tag;

    @Column(name = "jwp_date")
    private LocalDate jwpDate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean activated;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_secret", nullable = false)
    private boolean isSecret;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int view;


    @Builder
    private Post(String title, String description, TeamTag tag, Long writerId, boolean activated, boolean isSecret, LocalDate jwpDate, String imageUrl, int view) {
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

    public static Post create(final Long writerId, final String title, final String description, final TeamTag tag, final LocalDate jwpDate, final String imageUrl) {
        return Post.builder()
                .writerId(writerId)
                .title(title)
                .description(description)
                .tag(tag)
                .activated(true)
                .isSecret(false)
                .jwpDate(jwpDate)
                .imageUrl(imageUrl)
                .build();
    }

    public void updateAll(final String title, final String description, final TeamTag tag, final boolean isSecret, final LocalDate jwpDate, final String imageUrl) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.isSecret = isSecret;
        this.jwpDate = jwpDate;
        this.imageUrl = imageUrl;
    }

    public void delete() {
        this.activated = false;
    }
}

