package com.msws.shareplates.biz.file.controller;


import com.msws.shareplates.biz.file.entity.FileInfo;
import com.msws.shareplates.biz.file.service.FileInfoService;
import com.msws.shareplates.framework.annotation.AdminOnly;
import com.msws.shareplates.framework.config.FileConfig;
import com.msws.shareplates.framework.session.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Slf4j
@RequestMapping("/files")
@RestController
public class FileUploadController {

    @Autowired
    MessageSourceAccessor messageSourceAccessor;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private FileConfig fileConfig;

    @AdminOnly
    @GetMapping("/uploadAllFiles")
    public List<FileInfo> uploaedFileList() {

        log.debug("admin only");
        return fileInfoService.selectFileList();

    }
	/*

    @PostMapping("/uploadFile")
    public FileInfoResponse uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req) {

		String fileName = file.getName();
		String result = "";

			
		fileName = fileInfoService.storeFile(file, req);
		result = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/file/downloadFile/")
            .path(fileName)
            .toUriString();
		
        return new FileInfoResponse(fileName, result);
    }

	@RequestMapping(value = "/uploadMultipleFiles", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    public List<FileInfoResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest req) {
    	
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, req))
                .collect(Collectors.toList());
    }
    */


    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request, UserInfo userInfo) {
        // Load file as Resource
        Resource resource = fileInfoService.loadFileAsResource(fileName, userInfo);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, @RequestParam("uuid") String uuid, HttpServletRequest request, UserInfo userInfo) {

        FileInfo fileInfo = fileInfoService.selectFileInfo(fileId, uuid);
        Resource resource = fileInfoService.loadFileAsResource(fileInfo.getPath(), userInfo);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getName() + "\"")
                .body(resource);
    }
}
