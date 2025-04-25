package com.playus.communityservice.domain.post.entity;

import com.playus.communityservice.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;


    @Builder
    private PostImage(Post post, File file) {
        this.post = post;
        this.file = file;
    }

    public static PostImage create(Post post, File file) {
        return PostImage.builder()
                .post(post)
                .file(file)
                .build();
    }

    public void update(Post post, File file) {
        this.post = post;
        this.file = file;
    }
}
