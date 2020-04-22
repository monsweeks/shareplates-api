package com.msws.shareplates.framework.websocket;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.msws.shareplates.biz.share.entity.ShareUserSocket;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.message.service.ShareMessageService;
import com.msws.shareplates.framework.session.vo.UserInfo;
import com.msws.shareplates.framework.websocket.principal.StompPrincipal;

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
        StompPrincipal socketUserId = (StompPrincipal) generic.getHeaders().get("simpUser");
        System.out.println(socketUserId.getName());

        //TODO 사용자 ID를 가져와서 세팅에 추가
        ShareUserSocket shareUserSocket = ShareUserSocket.builder().sessionId(sessionId).build();
        shareService.createShareUserSocket(shareUserSocket);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        UserInfo info = (UserInfo) headerAccessor.getSessionAttributes().get("USER_INFO");

        if (info != null) {
            ShareUserSocket shareUserSocket = shareService.selectShareUserSocket(event.getSessionId());
            shareService.deleteShareUserSocket(shareUserSocket.getSessionId());
            Long count = shareService.countSessionByShareIdAndUserId(shareUserSocket.getShareUser().getShare().getId(), info.getId());

            if (count > 0L) {
                if (shareUserSocket.getShareUser().getStatus() != SocketStatusCode.ONLINE) {
                    shareService.updateStatusById(shareUserSocket.getShareUser().getId(), SocketStatusCode.ONLINE);
                    shareMessageService.sendUserStatusChange(shareUserSocket.getShareUser().getShare().getId(), info, SocketStatusCode.OFFLINE);
                }
            } else {
                if (shareUserSocket.getShareUser().getStatus() != SocketStatusCode.OFFLINE) {
                    shareService.updateStatusById(shareUserSocket.getShareUser().getId(), SocketStatusCode.OFFLINE);
                    shareMessageService.sendUserStatusChange(shareUserSocket.getShareUser().getShare().getId(), info, SocketStatusCode.OFFLINE);
                }
            }

        } else {
            shareService.deleteShareUserSocket(event.getSessionId());
        }
        log.info("[Disconnected] websocket");
    }
}
