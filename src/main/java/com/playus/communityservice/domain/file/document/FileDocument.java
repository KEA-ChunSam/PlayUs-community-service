package com.playus.communityservice.domain.file.document;

import com.playus.communityservice.domain.file.entity.File;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
