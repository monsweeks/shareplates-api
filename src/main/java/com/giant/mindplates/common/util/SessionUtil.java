package com.giant.mindplates.common.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.framework.session.vo.UserInfo;

@Component
public class SessionUtil {

    @Value("${shareplates.locale.default}")
    private String defaultLanguage;

    public SessionUtil() {

    }

    public boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return Optional.<UserInfo>ofNullable((UserInfo) session.getAttribute("userInfo")).isPresent();
            
        }else {
        	return false;
        }
    }


    public boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "Y".equals(session.getAttribute("adminYn"));
    }
    
    public void login(HttpServletRequest request, Long id) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);
        
        UserInfo info = UserInfo.builder()
        		.id(id)
        		.build();
        
        session.setAttribute("userInfo", info);

    }
    

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

    }
    
    public static UserInfo getUserInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        
        if (session != null) {
            return  (UserInfo) session.getAttribute("userInfo");
        }else {
        	throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER);
        }
    	
    }

    public Long getUserId(HttpServletRequest request) {
        Long id = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object value = session.getAttribute("id");
            if (value != null) {
                id = (Long)value;
            }
        }

        return id;
    }

}
