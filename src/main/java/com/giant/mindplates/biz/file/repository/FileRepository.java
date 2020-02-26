package com.giant.mindplates.biz.file.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giant.mindplates.biz.file.entity.UploadedFileInfo;

public interface FileRepository extends JpaRepository<UploadedFileInfo, Long> {
    
	Optional<UploadedFileInfo> findByName (String name);
}

