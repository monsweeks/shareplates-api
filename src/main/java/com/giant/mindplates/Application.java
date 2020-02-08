package com.giant.mindplates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})	//TODO DB 사용어떤거 할지 결정되면 제거
public class Application extends SpringBootServletInitializer{

	//스프링부트 톰캣 이용시
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
