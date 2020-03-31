package com.msws.shareplates.common.exception;

import com.msws.shareplates.common.exception.code.VendorExceptionCode;

import lombok.Getter;

@Getter
public class VendorException extends CommonException{

	private VendorExceptionCode vendorExceptionCode;
	private String[] messageParameters;
	
	public VendorException(VendorExceptionCode vendorExceptionCode) {
		this.vendorExceptionCode = vendorExceptionCode;
	}
	
	public VendorException(VendorExceptionCode vendorExceptionCode, String[] messageParameters) {
		this.vendorExceptionCode = vendorExceptionCode;
		this.messageParameters = messageParameters;
	}
}
