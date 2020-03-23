package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum StatusCode {

	CREATE("생성"),
	UPDATE("수정"),
	DELETE("삭제"),
	;
	
	private String desc;
}
