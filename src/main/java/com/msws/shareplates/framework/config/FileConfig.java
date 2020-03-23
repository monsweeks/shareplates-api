package com.msws.shareplates.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@ConfigurationProperties(prefix = "file")
public class FileConfig {
	
	private String uploadDir;
	private String allowedExtension;

}
