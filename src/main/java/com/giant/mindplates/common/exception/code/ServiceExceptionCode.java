package com.giant.mindplates.common.exception.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ServiceExceptionCode {

	BAD_REQUEST(HttpStatus.BAD_REQUEST, "request.error.msg"),
	EXIST_EMAIL(HttpStatus.CONFLICT, "mail.exist.msg");
	
	private HttpStatus code;
	private String messageCode;
}
