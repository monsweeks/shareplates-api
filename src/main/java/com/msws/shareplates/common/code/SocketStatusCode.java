package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum SocketStatusCode {
	
	ONLINE("온라인"),
	OFFLINE("오프라인");
	
	private String desc;

}
