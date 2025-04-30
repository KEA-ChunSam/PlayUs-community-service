package com.playus.communityservice.domain.file.document;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(value = "file")
public class FileDocument {

    @Id
    private Long id;

    @NotNull
    @Field(name = "image_URL")
    private String imageURL;

    @Builder
    private FileDocument(Long id, String imageURL) {
        this.id = id;
        this.imageURL = imageURL;
    }

    public static FileDocument createForOnlyTest(Long id, String imageURL) {
        return FileDocument.builder()
                .id(id)
                .imageURL(imageURL)
                .build();
    }
}
