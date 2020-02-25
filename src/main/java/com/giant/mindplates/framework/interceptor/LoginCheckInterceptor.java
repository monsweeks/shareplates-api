package com.giant.mindplates.framework.interceptor;

import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    private final SessionUtil sessionUtil;

    private final MessageSourceAccessor messageSourceAccessor;

    public LoginCheckInterceptor(SessionUtil sessionUtil,  MessageSourceAccessor messageSourceAccessor) {
        this.sessionUtil = sessionUtil;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        DisableLogin disableLogin = null;

        if (handler instanceof HandlerMethod) {
            disableLogin = ((HandlerMethod) handler).getMethodAnnotation(DisableLogin.class);
        }

        if (disableLogin != null) {
            return true;
        }

        if (!sessionUtil.isLogin(request)) {
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("{\"code\" : \"E001\", \"title\" : \"로그인 되어 있지 않습니다.\", \"message\" : \"" + messageSourceAccessor.getMessage("session.expired.msg") + "\"}");
            return false;
        }

        return true;
    }

}
