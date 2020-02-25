package com.giant.mindplates.common.exception;

import com.giant.mindplates.common.exception.code.ServiceExceptionCode;

import lombok.Getter;

@Getter
public class ServiceException extends CommonException{

	private ServiceExceptionCode serviceExceptionCode;
	
	public ServiceException(ServiceExceptionCode serviceExceptionCode) {
		this.serviceExceptionCode = serviceExceptionCode;
	}
}
