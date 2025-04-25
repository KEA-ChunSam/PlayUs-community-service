package com.playus.communityservice.domain.post.entity;

import com.playus.communityservice.domain.common.BaseTimeEntity;
import com.playus.communityservice.domain.post.enums.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private Tag tag;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean activated;

    @Column(name = "is_secret", nullable = false)
    private boolean isSecret;


    @Builder
    private Post(String title, String description, Tag tag, Long writerId, boolean activated, boolean isSecret) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.writerId = writerId;
        this.activated = activated;
        this.isSecret = isSecret;
    }

    public static Post create(final Long writerId, final String title, final String description, final Tag tag) {
        return Post.builder()
                .writerId(writerId)
                .title(title)
                .description(description)
                .tag(tag)
                .activated(true)
                .isSecret(false)
                .build();
    }

    public void updateAll(final String title, final String description, final Tag tag, final boolean isSecret) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.isSecret = isSecret;
    }

    public void delete() {
        this.activated = false;
    }
}

