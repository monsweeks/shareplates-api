package com.msws.shareplates.common.exception;

import lombok.Getter;

@Getter
public class StatDBException extends CommonException{

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public StatDBException(String message) {
		this.message = message;
	}

}
