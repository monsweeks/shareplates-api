package com.msws.shareplates.framework.websocket;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSocketEventListener {
	
	@Autowired
	private ShareService shareService;

	// TODO share-user테이블 접속 상태 업데이트
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(),
				SimpMessageHeaderAccessor.class);

		GenericMessage generic = (GenericMessage) accessor.getHeader("simpConnectMessage");
		Map nativeHeaders = (Map) generic.getHeaders().get("nativeHeaders");
		String sessionId = (String) generic.getHeaders().get("simpSessionId");

	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		UserInfo info = (UserInfo) headerAccessor.getSessionAttributes().get("USER_INFO");
		
		if(info != null) {			
			shareService.updateStatusByUudi(SocketStatusCode.OFFLINE, info.getUuid());
			
			shareService.selectShareUserByUuid(info.getUuid()).ifPresent(shareUser -> {
				//TODO 오프라인 정보 전달
				System.out.println(shareUser.getId());
			});
		}
		log.info("[Disconnected] websocket");
	}
}
