package com.playus.communityservice.domain.file.repository.read;

import com.playus.communityservice.domain.file.document.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileReadOnlyRepository extends MongoRepository<FileDocument, Long> {
}
