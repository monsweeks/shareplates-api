package com.msws.shareplates.framework.redis.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msws.shareplates.common.message.MessageBroker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TopicMessageListener implements MessageListener {
	
	private RedisTemplate redisTemplate;

	@Autowired
	private MessageBroker messageBroker;
	
	@Autowired
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		byte[] body = message.getBody();
		
		String str = (String) redisTemplate.getStringSerializer().deserialize(body);
		
		try {
			messageBroker.sendMessage(str);
		} catch (JsonProcessingException e) {
			
			log.error(e.getLocalizedMessage(), e);
		}
	}
}