package com.giant.mindplates.common.exception.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ServiceExceptionCode {

	//요청
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "common.error.badRequest"),
	//이메일
	EXIST_EMAIL(HttpStatus.CONFLICT, "user.error.alreadyRegisterd"),	
	
	//세션
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "session.error.expired"),
	
	//파일
	FILE_ALREADY_EXIST(HttpStatus.CONFLICT, "file.error.uploadAlreadyExists"),
	FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "file.error.uploadfail"),
	FILE_NOT_ALLOW_EXTENTION(HttpStatus.NOT_ACCEPTABLE, "file.error.uploadextension"),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "file.error.resourceNotFound"),
	;
	private HttpStatus code;
	private String messageCode;
}
