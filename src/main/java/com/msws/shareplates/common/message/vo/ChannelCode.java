package com.msws.shareplates.common.message.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ChannelCode {
	
	CHAT("chat", "채팅"), 
	URL("url", "URL 공유");
	
	private String code;
	private String desc;

}
