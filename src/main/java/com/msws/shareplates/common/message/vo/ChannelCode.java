package com.msws.shareplates.common.message.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ChannelCode {
	
	SHARE_ROOM("share-room", "공유"),
	CHAT("chat", "채팅"), 
	URL("url", "URL 공유");
	
	private String code;
	private String desc;

}
