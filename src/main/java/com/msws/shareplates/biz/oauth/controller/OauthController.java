package com.msws.shareplates.biz.oauth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("oauth")
@RestController	
public class OauthController {
	
	private final String service_prefix = "OAUTH_KAKAO_";

	@Autowired
	private OauthServiceFactory serviceFactory;
	
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
	@GetMapping(value = "/{oauth_vendor}/token")
	public TokenInfo getAuthorizationCode(@PathVariable(name = "oauth_vendor") OauthVendor oauth_vendor, 
										  HttpServletRequest req, 
										  String code) {
		
		log.info("received authorization code is {}", code);
		
		OauthServiceIF service = serviceFactory.getOauthVendorService(oauth_vendor);
		
		String result = service.getToken(code);
		TokenInfo ti = new Gson().fromJson(result, TokenInfo.class);

		OauthUserInfo user_info = service.getUserInfo(ti.getAccess_token()); 
		
		User user = User.builder().build();
		user.setPassword("password");
		user.setName(user_info.getNickname());
		user.setEmail(service_prefix + user_info.getEmail());
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
	@PostMapping(value = "/{oauth_vendor}//token")
	public TokenInfo refreshToken(@PathVariable(name = "oauth_vendor") OauthVendor oauth_vendor,String refresh_token) {
		
		OauthServiceIF service = serviceFactory.getOauthVendorService(oauth_vendor);
		String result = service.refreshToken(refresh_token);
		TokenInfo tr = new Gson().fromJson(result, TokenInfo.class);
		log.info("received token info is {}", tr);
		return tr; 
	}
	
	
		

}
