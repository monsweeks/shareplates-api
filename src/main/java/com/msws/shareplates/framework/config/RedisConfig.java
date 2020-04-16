package com.msws.shareplates.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msws.shareplates.framework.redis.listener.TopicMessageListener;
import com.msws.shareplates.framework.redis.template.JsonRedisTemplate;

@Configuration
public class RedisConfig {

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
	    container.addMessageListener(topicMessageListener(), new PatternTopic("sendMessage"));
		
		return container;
	}

	@Bean
	public TopicMessageListener topicMessageListener() {
		return new TopicMessageListener();
	}

	@Bean
	public JsonRedisTemplate jsonRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
		return new JsonRedisTemplate<>(connectionFactory, objectMapper, Object.class);
	}
}
