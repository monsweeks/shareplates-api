package com.msws.shareplates.biz.file.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.msws.shareplates.biz.file.entity.UploadedFileInfo;
import com.msws.shareplates.biz.file.service.FileUploadService;
import com.msws.shareplates.biz.file.vo.UploadFileResponse;
import com.msws.shareplates.framework.annotation.AdminOnly;
import com.msws.shareplates.framework.config.FileConfig;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RequestMapping("/file")
@RestController
public class FileUploadController {
	
	@Autowired
    private FileUploadService fileStorageService;
	
	@Autowired
    private FileConfig fileConfig;
	
	@Autowired
    MessageSourceAccessor messageSourceAccessor;
	
	@AdminOnly
	@GetMapping("/uploadAllFiles")
	public List<UploadedFileInfo> uploaedFileList(){
		
		log.debug("admin only");
		return fileStorageService.selectFileList();
		
	}
	
	@GetMapping("/uploadFiles")
	public List<UploadedFileInfo> uploaedFileListByUserId(UserInfo userInfo){
		
		log.debug("admin only");
		return fileStorageService.selectFileListByUserId(userInfo);
		
	}
	
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req) {

		String fileName = file.getName();
		String result = "";
			
		fileName = fileStorageService.storeFile(file, req);
		result = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/file/downloadFile/")
            .path(fileName)
            .toUriString();
		
        return new UploadFileResponse(fileName, result);
    }

	@RequestMapping(value = "/uploadMultipleFiles", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest req) {
    	
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, req))
                .collect(Collectors.toList());
    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request, UserInfo userInfo) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, userInfo);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
