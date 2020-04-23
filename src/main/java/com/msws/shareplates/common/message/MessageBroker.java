package com.msws.shareplates.common.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msws.shareplates.common.message.vo.MessageInfo;
import com.msws.shareplates.framework.redis.template.JsonRedisTemplate;

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
	
	public void pubMessage(MessageInfo info) {
			
		jsonRedisTemplate.convertAndSend("sendMessage" , info);
	}
	
	public void sendMessage(String str) throws JsonMappingException, JsonProcessingException {
		MessageInfo message = mapper.readValue(str, MessageInfo.class);		

		log.info("send url -> {}", message.targetTopicUrl());
		
		if(message.getUserId() == null || "".equals(message.getUserId())) {
			simpMessagingTemplate.convertAndSend(message.targetTopicUrl(), message.getData());
		}else {		
			simpMessagingTemplate.convertAndSendToUser(message.getUserId(), message.targetTopicUrl(), message.getData());
		}
	}
}
