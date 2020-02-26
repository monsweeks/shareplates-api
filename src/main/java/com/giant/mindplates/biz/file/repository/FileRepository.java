package com.giant.mindplates.biz.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giant.mindplates.biz.file.entity.UploadedFileInfo;

public interface FileRepository extends JpaRepository<UploadedFileInfo, Long> {
	List<UploadedFileInfo> findByOwner (String owner);
}

