package com.giant.mindplates.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.framework.exception.AuthenticationException;
import com.giant.mindplates.util.SessionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    private final SessionUtil sessionUtil;

    private final MessageSourceAccessor messageSourceAccessor;
    
    private final String activeProfile; 

    public LoginCheckInterceptor(SessionUtil sessionUtil, MessageSourceAccessor messageSourceAccessor, String activeProfile) {
        this.sessionUtil = sessionUtil;
        this.messageSourceAccessor = messageSourceAccessor;
        this.activeProfile = activeProfile;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        DisableLogin disableLogin = null;

        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            disableLogin = ((HandlerMethod) handler).getMethodAnnotation(DisableLogin.class);
        }

        if (disableLogin != null) {
            return true;
        }

        if (!sessionUtil.isLogin(request)) {
            throw new AuthenticationException(messageSourceAccessor.getMessage("error.sessionExpired"));
        }

        return true;
    }

}
