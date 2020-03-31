package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AuthCode {

	WRITE("WRITE"),
	READ("READ"),
    NONE("NONE");
	private String code;
}
