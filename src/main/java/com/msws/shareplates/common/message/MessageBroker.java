package com.msws.shareplates.common.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msws.shareplates.common.message.vo.MessageInfo;
import com.msws.shareplates.common.message.vo.MessageInfo.SenderInfo;
import com.msws.shareplates.framework.redis.template.JsonRedisTemplate;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageBroker {

	@Autowired
	private JsonRedisTemplate<MessageInfo> jsonRedisTemplate;
	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private ObjectMapper mapper;
	
	public void pubMessage(String topicUrl, Object messageObject, UserInfo info) {
			
		jsonRedisTemplate.convertAndSend("sendMessage"
				, MessageInfo.builder()
					.topicUrl(topicUrl)
					.messageObject(messageObject)
					.senderInfo(SenderInfo.builder().id(info.getId()).uuid(info.getUuid()).build())
					.build());
	}
	
	public void sendMessage(String str) throws JsonMappingException, JsonProcessingException {
		MessageInfo message = mapper.readValue(str, MessageInfo.class);
		
		log.info("send url -> {}", message.targetTopicUrl());
		
		simpMessagingTemplate.convertAndSend(message.targetTopicUrl(), message.getMessageObject());
	}
}
