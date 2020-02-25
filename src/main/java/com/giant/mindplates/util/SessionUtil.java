package com.giant.mindplates.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtil {

    @Value("${shareplates.locale.default}")
    private String defaultLanguage;

    public SessionUtil() {

    }

    public boolean isLogin(HttpServletRequest request) {
        boolean login = false;
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long id = (Long) session.getAttribute("id");
            if (id != null) {
                login = true;
            }
        }

        return login;
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
        session.setAttribute("id", id);

    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
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
