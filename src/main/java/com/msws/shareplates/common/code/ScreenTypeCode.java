package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ScreenTypeCode {

	WEB("WEB"),
	PROJECTOR("PROJECTOR"),
	CONTROLLER("CONTROLLER");
	private String code;

}
