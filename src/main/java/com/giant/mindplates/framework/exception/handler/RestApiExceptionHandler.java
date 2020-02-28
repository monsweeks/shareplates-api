package com.giant.mindplates.framework.exception.handler;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.framework.exception.vo.ErrorResponse;

@RestControllerAdvice
public class RestApiExceptionHandler {
	
	@Autowired
	private MessageSourceAccessor messageSourceAccessor;
	
	BiFunction<ServiceExceptionCode, String[], ResponseEntity<ErrorResponse>> response = (serviceExceptionCode, args) -> {
		String message = messageSourceAccessor.getMessage(serviceExceptionCode.getMessageCode(), args);
		
		return new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
				.code(serviceExceptionCode.getCode().name())
				.message(message)
				.build(), serviceExceptionCode.getCode());
	};	

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> handleServiceException(ServiceException e) {
		return response.apply(e.getServiceExceptionCode(), e.getMessageParameters());
	}
}
