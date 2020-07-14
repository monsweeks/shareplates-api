package com.msws.shareplates.biz.oauth.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.msws.shareplates.biz.oauth.entity.OauthUserInfo;
import com.msws.shareplates.biz.oauth.service.OauthServiceFactory;
import com.msws.shareplates.biz.oauth.service.IF.OauthServiceIF;
import com.msws.shareplates.biz.oauth.vo.OauthVendor;
import com.msws.shareplates.biz.oauth.vo.response.TokenInfo;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.DisableLogin;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("/api/oauth")
@Controller
public class OauthController {

    @Autowired
    private OauthServiceFactory serviceFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtil sessionUtil;

    @Value("${shareplates.web-url}")
    private String redirectUrl;

    @DisableLogin
    @GetMapping(value = "/{oauth-vendor}/token")
    public String selectAuthorizationCode(@PathVariable(name = "oauth-vendor") OauthVendor oauthVendor,
                                          HttpServletRequest req, HttpServletResponse res,
                                          String code) {

        log.info("received authorization code is {}", code);

        OauthServiceIF service = serviceFactory.getOauthVendorService(oauthVendor);

        String result = service.getToken(code);
        TokenInfo ti = new Gson().fromJson(result, TokenInfo.class);
        
        log.info("received token info is {}", ti);
        
        OauthUserInfo user_info = service.getUserInfo(ti.getAccess_token());

        User user = User.builder()
                .password(UUID.randomUUID().toString())
                .name(user_info.getNickname())
                .email(user_info.getEmail())
                .activateMailSendResult(true)
                .activateYn(true)
                .build();

        User siteUser;
        if (userService.checkEmail(user.getEmail())) {
            siteUser = userService.selectUserByEmail(user.getEmail());
        } else {
            user.setRegistered(false);
            siteUser = userService.createUser(user, true);
        }

        sessionUtil.login(req, siteUser, siteUser.getRegistered());
        
        log.info("session logined : {}", SessionUtil.getUserId(req));

        Cookie customCookie = new Cookie("JSESSIONID", req.getSession(false).getId());
        customCookie.setDomain("www.mindplates.com");
        customCookie.setPath("/");
        res.addCookie(customCookie);
        // 비밀번호가 등록되었다면
        if (siteUser.getRegistered() != null && siteUser.getRegistered() == true) {
            return "redirect:" + redirectUrl;
        } else {
            return "redirect:" + redirectUrl + "users/register";
        }
    }

    @DisableLogin
    @PostMapping(value = "/{oauth-vendor}/token")
    public TokenInfo refreshToken(@PathVariable(name = "oauth-vendor") OauthVendor oauthVendor, String refreshToken) {

        OauthServiceIF service = serviceFactory.getOauthVendorService(oauthVendor);
        String result = service.refreshToken(refreshToken);
        TokenInfo tr = new Gson().fromJson(result, TokenInfo.class);
        log.info("received token info is {}", tr);
        return tr;
    }


}
