package com.msws.shareplates.common.message.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo {

	private MessageData data;
	private SenderInfo senderInfo;
	private String topicUrl;
	
	public String targetTopicUrl() {
		return "/sub/" + topicUrl;
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SenderInfo{
		private long id;
		private String email;
		private String name;
		private String uuid;
	}
}
