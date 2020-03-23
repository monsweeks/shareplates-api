package com.msws.shareplates.common.exception;

import com.msws.shareplates.common.exception.code.ServiceExceptionCode;

import lombok.Getter;

@Getter
public class ServiceException extends CommonException{

	private ServiceExceptionCode serviceExceptionCode;
	private String[] messageParameters;
	
	public ServiceException(ServiceExceptionCode serviceExceptionCode) {
		this.serviceExceptionCode = serviceExceptionCode;
	}
	
	public ServiceException(ServiceExceptionCode serviceExceptionCode, String[] messageParameters) {
		this.serviceExceptionCode = serviceExceptionCode;
		this.messageParameters = messageParameters;
	}
}
