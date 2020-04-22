package com.msws.shareplates.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.msws.shareplates.framework.websocket.interceptor.WebSocketInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	@Autowired
	private WebSocketInterceptor webSocketInterceptor;
	
	public void configureMessageBroker(MessageBrokerRegistry brokerRegistry) {
		brokerRegistry.enableSimpleBroker("/sub");
		brokerRegistry.setApplicationDestinationPrefixes("/pub");
	}
	
	public void registerStompEndpoints(StompEndpointRegistry endpointRegistry) {
		endpointRegistry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS().setInterceptors(webSocketInterceptor);
	}

}
