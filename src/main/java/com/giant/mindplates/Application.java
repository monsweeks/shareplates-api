package com.giant.mindplates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    //스프링부트 톰캣 이용시
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
