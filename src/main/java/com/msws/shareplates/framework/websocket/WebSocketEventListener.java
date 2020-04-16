package com.msws.shareplates.framework.websocket;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketEventListener {

		//TODO share-user테이블 접속 상태 업데이트
	    @EventListener
	    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
	        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
	        GenericMessage generic = (GenericMessage) accessor.getHeader("simpConnectMessage");
	        Map nativeHeaders = (Map) generic.getHeaders().get("nativeHeaders");
	        String sessionId = (String) generic.getHeaders().get("simpSessionId");

	        
	    }
	    
	    //TODO share-user테이블 접속 상태 업데이트
	    @EventListener
	    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
	        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

	        String sessionId = headerAccessor.getSessionId();

	        log.info("[Disconnected] websocket session id : {}", sessionId);
	    }
}
