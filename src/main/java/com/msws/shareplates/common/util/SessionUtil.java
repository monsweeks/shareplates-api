package com.msws.shareplates.common.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.framework.session.vo.UserInfo;

@Component
public class SessionUtil {

    @Value("${shareplates.locale.default}")
    private String defaultLanguage;

    public SessionUtil() {

    }

    public static UserInfo getUserInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            return (UserInfo) session.getAttribute("userInfo");
        } else {
            // 로그인 안된 사용자도 접근할 수 있도록 없으면 없는대로 내려주도록 변경
            return null;
        }

    }

    public static Long getUserId(HttpServletRequest request) {
        Long id = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
            if (userInfo != null) {
                id = userInfo.getId();
            }
        }

        return id;
    }

    public boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return Optional.<UserInfo>ofNullable((UserInfo) session.getAttribute("userInfo")).isPresent();

        } else {
            return false;
        }
    }

    public boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "Y".equals(session.getAttribute("adminYn"));
    }

    public void login(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);

        UserInfo info = UserInfo.builder()
                .id(user.getId())
                .roleCode(user.getActiveRoleCode())
                .registered(true)
                .build();

        session.setAttribute("userInfo", info);

    }

    public void login(HttpServletRequest request, Long id, Boolean register) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);

        UserInfo info = UserInfo.builder()
                .id(id)
                .registered(register)
                .build();

        session.setAttribute("userInfo", info);

    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

    }

}
