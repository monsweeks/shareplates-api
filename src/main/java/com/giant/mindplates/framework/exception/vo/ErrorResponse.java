package com.giant.mindplates.framework.exception.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

	private String errorMessage;
	private String code;
}
