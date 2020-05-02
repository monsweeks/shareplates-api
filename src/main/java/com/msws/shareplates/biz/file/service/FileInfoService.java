package com.msws.shareplates.biz.file.service;

import com.msws.shareplates.biz.file.entity.FileInfo;
import com.msws.shareplates.biz.file.repository.FileInfoRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.config.FileConfig;
import com.msws.shareplates.framework.session.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FileInfoService {

    private final List<String> allowedExtensions;
    private final Path fileStorageLocation;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    public FileInfoService(FileConfig fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        this.allowedExtensions = Arrays.asList(fileStorageProperties.getAllowedExtension().split(","));

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ServiceException(ServiceExceptionCode.FILE_UPLOAD_FAIL);
        }
    }

    public List<FileInfo> selectFileList() {
        return fileInfoRepository.findAll();
    }

    public String storeFile(MultipartFile file, HttpServletRequest req) {

        if (!allowedExtensions.stream().anyMatch(p -> file.getOriginalFilename().endsWith(p))) throw new ServiceException(ServiceExceptionCode.FILE_NOT_ALLOW_EXTENTION, new String[]{String.join(",", allowedExtensions)});

        Path path = this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString() + File.separator + file.getOriginalFilename());
        while (Files.exists(path)) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            path = this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString() + File.separator + file.getOriginalFilename() + "." + timestamp.getTime());
        }

        Path targetLocation = null;
        String fileName = StringUtils.cleanPath(path.getFileName().toString());

        try {
            Path userUploadDir = this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString());
            if (!Files.exists(userUploadDir)) Files.createDirectories(userUploadDir.normalize());
            targetLocation = userUploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return sessionUtil.getUserId(req).toString() + File.separator + fileName;
        } catch (Exception ex) {
            try {
                if (targetLocation != null) Files.deleteIfExists(targetLocation);
            } catch (IOException e) {
                log.error("File delete error", e);
            }
            throw new ServiceException(ServiceExceptionCode.FILE_UPLOAD_FAIL);
        }

    }

    public Resource loadFileAsResource(String path) {
        try {
            Path filePath = this.fileStorageLocation.resolve(path).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ServiceException(ServiceExceptionCode.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            throw new ServiceException(ServiceExceptionCode.FILE_NOT_FOUND);
        }
    }

    private FileInfo saveStoredFileInfo(MultipartFile file, HttpServletRequest req) {

        FileInfo data = new FileInfo();

        LocalDateTime now = LocalDateTime.now();
        data.setCreationDate(now);
        data.setLastUpdateDate(now);
        data.setSize(file.getSize());
        data.setName(file.getOriginalFilename());

        fileInfoRepository.saveAndFlush(data);
        data.setCreatedBy(sessionUtil.getUserId(req));
        data.setLastUpdatedBy(sessionUtil.getUserId(req));
        return fileInfoRepository.save(data);

    }

    public FileInfo createFileInfo(FileInfo fileInfo) {
        return fileInfoRepository.saveAndFlush(fileInfo);
    }

    public FileInfo selectFileInfo(long fileId, String uuid) {
        return fileInfoRepository.findByIdAndUuid(fileId, uuid).orElseThrow(() -> new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND));
    }

}
