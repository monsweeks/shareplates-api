package com.msws.shareplates.common.exception.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum VendorExceptionCode {
	
	NOT_SUPPORTED_VENDOR(HttpStatus.BAD_REQUEST, "vendor.error.not_supported_oauth_vendor"),
	
	KAKAO_OAUTH2_SERVICE_NOT_AVAILABLE(HttpStatus.CONFLICT, "vendor.error.kakao_service_not_available"),
	;
	
    private HttpStatus code;
    private String messageCode;
}
