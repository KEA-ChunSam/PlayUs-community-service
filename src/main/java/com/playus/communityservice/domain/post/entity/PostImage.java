package com.playus.communityservice.domain.post.entity;

import com.playus.communityservice.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void update(File file) {
        this.file = file;
    }
}
