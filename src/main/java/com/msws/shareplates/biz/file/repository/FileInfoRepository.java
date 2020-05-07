package com.msws.shareplates.biz.file.repository;

import com.msws.shareplates.biz.file.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    Optional<FileInfo> findByIdAndUuid(long id, String uuid);

}

