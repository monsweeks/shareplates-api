package com.msws.shareplates.common.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmptyResponse {
	
	private static EmptyResponse emptyResponse = new EmptyResponse();
	
	public static EmptyResponse getInstance() {
		return emptyResponse;
	}

}
