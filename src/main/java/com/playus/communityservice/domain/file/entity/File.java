package com.playus.communityservice.domain.file.entity;

import com.playus.communityservice.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "files")
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_URL", nullable = false)
    private String imageURL;

    @Builder
    private File(String imageURL) {
        this.imageURL = imageURL;
    }

    public static File create(String imageURL) {
        return File.builder()
                .imageURL(imageURL)
                .build();
    }

    public void update(String imageURL) {
        this.imageURL = imageURL;
    }

}
