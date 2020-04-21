package com.msws.shareplates.framework.websocket.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		System.out.println("Channel Interceptor");

		MessageHeaders headers = message.getHeaders();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		System.out.println(accessor.getDestination());
		MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
		if (multiValueMap != null) {
			for (Map.Entry<String, List<String>> head : multiValueMap.entrySet()) {
				if (head != null) {
					System.out.println(head.getKey() + "#" + head.getValue());
				}
			}
		}
		
		return message;
	}

}
