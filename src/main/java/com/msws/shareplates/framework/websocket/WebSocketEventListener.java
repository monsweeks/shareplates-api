package com.msws.shareplates.framework.websocket;

import java.util.List;
import java.util.Map;

import com.msws.shareplates.common.message.service.ShareMessageService;
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

	@Autowired
	private ShareMessageService shareMessageService;

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

		log.info(event.getSessionId());
		
		if(info != null) {
			// TODO 웹소켓 1개 세션이므로, 이 세션이 속한 SHARE_USER 데이터만 오프라인해야하는데,
			// SHARE_USER의 UUID는 로그인당 생성되므로,
			// 사용자가 브라우저 탭을 3개 열고, 1개를 닫으면, 데이터 상으로 다 오프라인으로 처리될듯
			// 따라서 소켓 세션당 유일한 아이디가 필요
			shareService.updateStatusByUudi(SocketStatusCode.OFFLINE, info.getUuid());
			shareService.selectShareUserByUuid(info.getUuid()).ifPresent(shareUser -> {
				shareMessageService.sendUserStatusChange(shareUser.getShare().getId(), info, SocketStatusCode.OFFLINE);
				System.out.println(shareUser.getId());
			});
		}
		log.info("[Disconnected] websocket");
	}
}
