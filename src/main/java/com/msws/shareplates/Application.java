package com.msws.shareplates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import com.msws.shareplates.framework.config.FileConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableConfigurationProperties({
    FileConfig.class
})
@EnableAsync
@SpringBootApplication
@EnableAspectJAutoProxy
public class Application extends SpringBootServletInitializer implements CommandLineRunner{
	
	@Value("${spring.profiles.active}")
    private String activeProfile;
	
	@Value("${file.upload-dir}")
    private String fileupload_dir;
	
    //스프링부트 톰캣 이용시
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		log.info("stared as {}", activeProfile);
		log.info("fileupload dir {}", fileupload_dir);

		//code to do
		
	}
    
    

}
