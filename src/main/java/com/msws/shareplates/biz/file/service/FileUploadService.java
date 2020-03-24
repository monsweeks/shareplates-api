package com.msws.shareplates.biz.file.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.msws.shareplates.biz.file.entity.UploadedFileInfo;
import com.msws.shareplates.biz.file.repository.FileRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.config.FileConfig;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUploadService {

	@Autowired
	private SessionUtil sessionUtil;

	@Autowired
	private FileRepository fileRepository;

	private final List<String> allowedExtionis;

	private final Path fileStorageLocation;
	
	public List<UploadedFileInfo> selectFileList() {
        return fileRepository.findAll();
    }
	
	public List<UploadedFileInfo> selectFileListByUserId(UserInfo userInfo) {
        return fileRepository.findByOwner( String.valueOf(userInfo.getId()) );
    }
	
	

	@Autowired
	public FileUploadService(FileConfig fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		this.allowedExtionis = Arrays.asList(fileStorageProperties.getAllowedExtension());

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new ServiceException(ServiceExceptionCode.FILE_UPLOAD_FAIL);
		}
	}

	public String storeFile(MultipartFile file, HttpServletRequest req) {


		if (!allowedExtionis.stream().anyMatch(p -> file.getOriginalFilename().endsWith(p)))
			throw new ServiceException(ServiceExceptionCode.FILE_NOT_ALLOW_EXTENTION, new String[] {String.join(",", allowedExtionis)});
		
		if (Files.exists(this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString() + "/" + file.getOriginalFilename())))
			throw new ServiceException(ServiceExceptionCode.FILE_ALREADY_EXIST);

		Path targetLocation = null;
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			
			Path userUploadDir =  this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString());
			if(!Files.exists(userUploadDir))
				Files.createDirectories(userUploadDir.normalize());
			
			
			// Copy file to the target location (Replacing existing file with the same name)
			targetLocation = userUploadDir.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			saveStoredFileInfo(file, req);
			
			return fileName;
		} catch (Exception ex) {
			try {
				Files.deleteIfExists(targetLocation);
			} catch (IOException e) {
				log.error("File delete error", e);
			}
			throw new ServiceException(ServiceExceptionCode.FILE_UPLOAD_FAIL);
			
		}
		
	}

	public Resource loadFileAsResource(String fileName, UserInfo userInfo ) {
		try {
			Path filePath = this.fileStorageLocation.resolve(userInfo.getId()+ "/" + fileName).normalize();
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
	
	private UploadedFileInfo saveStoredFileInfo(MultipartFile file, HttpServletRequest req) {
		
		UploadedFileInfo data = new UploadedFileInfo();

		LocalDateTime now = LocalDateTime.now();
		data.setCreationDate(now);
		data.setLastUpdateDate(now);
		data.setOwner(sessionUtil.getUserId(req).toString());
		data.setSize(file.getSize());
		data.setName(file.getOriginalFilename());
		data.setUseYn(true);
		data.setShareYn(false);

		fileRepository.saveAndFlush(data);
		data.setCreatedBy(sessionUtil.getUserId(req));
		data.setLastUpdatedBy(sessionUtil.getUserId(req));
		return fileRepository.save(data);
		
	}

}
