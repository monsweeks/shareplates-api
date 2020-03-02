package com.giant.mindplates.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.common.util.SessionUtil;
import com.giant.mindplates.framework.annotation.DisableLogin;

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
        
        if(!sessionUtil.isLogin(request))
        	throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER);
        
        return true;
    }

}
