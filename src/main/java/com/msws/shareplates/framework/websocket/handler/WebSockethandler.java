package com.msws.shareplates.framework.websocket.handler;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.session.vo.UserInfo;
import com.msws.shareplates.framework.websocket.principal.StompPrincipal;

public class WebSockethandler extends DefaultHandshakeHandler {
	
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
		
		if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
                        
            UserInfo userInfo = SessionUtil.getUserInfo(req);
            
            if(userInfo != null) {                
                attributes.put("USER_INFO", userInfo);
            }
            return new StompPrincipal(String.valueOf(userInfo.getId()));
        }
		
		return null;
    }
}
