package com.msws.shareplates.framework.exception.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

	private String message;
	private String code;
}
