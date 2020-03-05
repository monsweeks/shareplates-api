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
            // 로그인 안된 사용자도 접근할 수 있도록 없으면 없는대로 내려주도록 변경
        	return null;
        }
    	
    }

    public static Long getUserId(HttpServletRequest request) {
        Long id = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
            if (userInfo != null) {
                id = userInfo.getId();
            }
        }

        return id;
    }

}
