package com.giant.mindplates.biz.file.service;

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

import com.giant.mindplates.biz.file.entity.UploadedFileInfo;
import com.giant.mindplates.biz.file.repository.FileRepository;
import com.giant.mindplates.common.exception.file.FileAlreadyExistException;
import com.giant.mindplates.common.exception.file.FileExtensionException;
import com.giant.mindplates.common.exception.file.FileStorageException;
import com.giant.mindplates.common.exception.file.MyFileNotFoundException;
import com.giant.mindplates.framework.config.FileConfig;
import com.giant.mindplates.util.SessionUtil;

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
	
	public List<UploadedFileInfo> selectFileListByUserId(HttpServletRequest req) {
        return fileRepository.findByOwner( sessionUtil.getUserId(req).toString() );
    }
	
	

	@Autowired
	public FileUploadService(FileConfig fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		this.allowedExtionis = Arrays.asList(fileStorageProperties.getAllowedExtension());

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public String storeFile(MultipartFile file, HttpServletRequest req) {


		if (!allowedExtionis.stream().anyMatch(p -> file.getOriginalFilename().endsWith(p)))
			throw new FileExtensionException("Could not store file. It's not allowed file Extension");
		
		if (Files.exists(this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString() + "/" + file.getOriginalFilename())))
			throw new FileAlreadyExistException("Could not store file. this file has already exist.");

		Path targetLocation = null;
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			
			Path userUploadDir =  this.fileStorageLocation.resolve(sessionUtil.getUserId(req).toString());
			if(!Files.exists(userUploadDir))
				Files.createDirectories(userUploadDir.normalize());
			
			
			// Copy file to the target location (Replacing existing file with the same name)
			targetLocation = userUploadDir.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			saveStoredFileInfo(file, req);
			
			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
			
		} catch (Exception ex) {
			
			try {
				Files.deleteIfExists(targetLocation);
			} catch (IOException e) {
				//nothing to do
			}
			throw new RuntimeException("Could not save file info to database, filename : " + fileName + ". Please try again!", ex);
			
		}
		
	}

	public Resource loadFileAsResource(String fileName, HttpServletRequest req ) {
		try {
			Path filePath = this.fileStorageLocation.resolve(sessionUtil.getUserId(req)+ "/" + fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
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
