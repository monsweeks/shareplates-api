package com.msws.shareplates.biz.oauth.kakao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.msws.shareplates.biz.oauth.kakao.entity.UserInfo;
import com.msws.shareplates.biz.oauth.kakao.service.OauthService;
import com.msws.shareplates.biz.oauth.vo.response.TokenInfo;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.DisableLogin;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("oauth/kakao")
@RestController
public class OauthController {
	
	private final String service_prefix = "OAUTH_KAKAO_";
	
	@Autowired
	private OauthService service;
	
	@Autowired
	private UserService user_service;
	
	@Autowired
	private SessionUtil session_util;
	


	
	/**
	 * get oauth token
	 * 
	 * @param req
	 * @param code
	 * @return
	 */
	@DisableLogin
	@GetMapping(value = "/token")
	public TokenInfo getAuthorizationCode(HttpServletRequest req, String code) {
		
		log.info("received authorization code is {}", code);
		
		String result = service.getToken(code);
		TokenInfo ti = new Gson().fromJson(result, TokenInfo.class);

		UserInfo user_info = new Gson().fromJson(service.getUserInfo(ti.getAccess_token()), UserInfo.class);
		
		User user = User.builder().build();
		user.setPassword("password");
		user.setName(user_info.getProperties().getNickname());
		user.setEmail(service_prefix + user_info.getKakao_account().getEmail());
		user.setActivateMailSendResult(true);
		user.setActivateYn(true);
		
		User site_user;
		if(user_service.checkEmail(user.getEmail())) {
			site_user = user_service.getUserByEmail(user.getEmail()).get();
			
		}else {
			site_user = user_service.createUser(user);
		}
		
		session_util.login(req, site_user.getId());
		
		log.info("received token info is {}", ti);
		return ti; 
	}
	
	/**
	 * refresh token
	 * 
	 * @param req
	 * @return
	 */
	@DisableLogin
	@PostMapping(value = "/token")
	public TokenInfo refreshToken(String refresh_token) {
		
		String result = service.refreshToken(refresh_token);
		TokenInfo tr = new Gson().fromJson(result, TokenInfo.class);
		log.info("received token info is {}", tr);
		return tr; 
	}
	
	
		

}
