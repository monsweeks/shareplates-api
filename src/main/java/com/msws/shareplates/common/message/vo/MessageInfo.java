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

	private String targetTopic;
	private String targetUser;
	private ChannelCode targetChannel;
	private Object messageObject;
	private SenderInfo senderInfo;
	
	public String targetTopicUrl() {
		return "/sub/" + targetTopic + "/" + targetUser + "/" + targetChannel.getCode();
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SenderInfo{
		private long id;
		private String email;
		private String name;
	}
}
