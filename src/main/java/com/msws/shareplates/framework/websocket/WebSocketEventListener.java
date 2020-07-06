package com.msws.shareplates.framework.websocket;

import com.msws.shareplates.biz.share.entity.ShareUserSocket;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.message.service.ShareMessageService;
import com.msws.shareplates.framework.session.vo.UserInfo;
import com.msws.shareplates.framework.websocket.principal.StompPrincipal;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Map;

@Component
@Slf4j
public class WebSocketEventListener {


    @Autowired
    private StatService statService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShareMessageService shareMessageService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);

        GenericMessage generic = (GenericMessage) accessor.getHeader("simpConnectMessage");
        Map nativeHeaders = (Map) generic.getHeaders().get("nativeHeaders");
        String sessionId = (String) generic.getHeaders().get("simpSessionId");
        StompPrincipal socketUserId = (StompPrincipal) generic.getHeaders().get("simpUser");

        // TODO 사용자 ID를 가져와서, ShareUserSocket에 추가하기
        ShareUserSocket shareUserSocket = ShareUserSocket.builder().sessionId(sessionId).build();
        shareService.createShareUserSocket(shareUserSocket);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // TODO ban 처리된 유저의 경우, join이 처리되지 않고, disconnected만 처리되므로, ban 처리 후 최초 1회만, state을 처리하도록 로직을 개선해야함
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
                // 통계 정보, 사용자 소켓이 1개 없어짐
                statService.writeDisconnectUserSession(shareUserSocket.getShareUser().getShare().getTopic().getId(), shareUserSocket.getShareUser().getShare().getId(), info.getId());
            } else {
                if (shareUserSocket.getShareUser().getStatus() != SocketStatusCode.OFFLINE) {
                    shareService.updateStatusById(shareUserSocket.getShareUser().getId(), SocketStatusCode.OFFLINE);
                    shareMessageService.sendUserStatusChange(shareUserSocket.getShareUser().getShare().getId(), info, SocketStatusCode.OFFLINE);
                }
                // 통계 정보, 사용자가 나감
                statService.writeDisconnectUser(shareUserSocket.getShareUser().getShare().getTopic().getId(), shareUserSocket.getShareUser().getShare().getId(), info.getId());
            }

            long focusedCount = shareService.selectFocusedSocketCount(shareUserSocket.getShareUser().getShare().getId(), info.getId());
            if (shareUserSocket.getShareUser().getFocusYn() != (focusedCount > 0)) {
                shareService.updateFocusById(shareUserSocket.getShareUser().getId(), focusedCount > 0);
                shareMessageService.sendUserFocusChange(shareUserSocket.getShareUser().getShare().getId(), info.getId(), focusedCount > 0, info);
                // 통계 정보, 사용자 포커스 정보가 변경됨
                statService.writeUserFocusChange(shareUserSocket.getShareUser().getShare().getTopic().getId(), shareUserSocket.getShareUser().getShare().getId(), info.getId(), focusedCount > 0);
            }

        } else {
            shareService.deleteShareUserSocket(event.getSessionId());
        }

        log.info("[Disconnected] websocket");


    }
}
