package com.playus.communityservice.domain.file.repository.write;

import com.playus.communityservice.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
