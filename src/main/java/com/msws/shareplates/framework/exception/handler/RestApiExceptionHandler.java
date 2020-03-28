package com.msws.shareplates.framework.exception.handler;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.VendorException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.exception.code.VendorExceptionCode;
import com.msws.shareplates.framework.exception.vo.ErrorResponse;

@RestControllerAdvice
public class RestApiExceptionHandler {
	
	@Autowired
	private MessageSourceAccessor messageSourceAccessor;
	
	BiFunction<ServiceExceptionCode, String[], ResponseEntity<ErrorResponse>> response = (serviceExceptionCode, args) -> {
		String message = messageSourceAccessor.getMessage(serviceExceptionCode.getMessageCode(), args);
		
		return new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
				.code(serviceExceptionCode.name())
				.message(message)
				.build(), serviceExceptionCode.getCode());
	};	
	
	BiFunction<VendorExceptionCode, String[], ResponseEntity<ErrorResponse>> vendorExcpetionResponse = (vendorExceptionCode, args) -> {
		String message = messageSourceAccessor.getMessage(vendorExceptionCode.getMessageCode(), args);
		
		return new ResponseEntity<ErrorResponse>(ErrorResponse.builder()
				.code(vendorExceptionCode.name())
				.message(message)
				.build(), vendorExceptionCode.getCode());
	};	

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> handleServiceException(ServiceException e) {
		return response.apply(e.getServiceExceptionCode(), e.getMessageParameters());
	}

	@ExceptionHandler(VendorException.class)
	public ResponseEntity<?> handleServiceException(VendorException e) {
		return vendorExcpetionResponse.apply(e.getVendorExceptionCode(), e.getMessageParameters());
	}
}
