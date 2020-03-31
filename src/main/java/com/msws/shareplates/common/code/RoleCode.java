package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RoleCode {
	
	MEMBER("MEMBER"),
	ADMIN("ADMIN");
	
	private String code;

}
