package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AuthCode {
	
	GENERAL_USER("일반 유저"),
	ADMIN("관리자");
	
	private String desc;

}
