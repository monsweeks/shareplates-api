package com.msws.shareplates.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ChatTypeCode {

	READY("READY"),
	SHARE("SHARE");
	private String code;

}
