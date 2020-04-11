package com.msws.shareplates.biz.oauth.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/oauth")
@Controller	
public class OauthController {
	
	private final String SERVICE_PREFIX = "OAUTH_{vendor}_";

	@Autowired
	private OauthServiceFactory serviceFactory;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionUtil sessionUtil;
	
	@Value("${shareplates.web-url}")
	private String redirectUrl;


	
	/**
	 * get oauth token
	 * 
	 * @param req
	 * @param code
	 * @return
	 */
	@DisableLogin
	@GetMapping(value = "/{oauth-vendor}/token")
	public String selectAuthorizationCode(@PathVariable(name = "oauth-vendor") OauthVendor oauthVendor,
										  HttpServletRequest req, 
										  String code) {
		
		log.info("received authorization code is {}", code);
		
		OauthServiceIF service = serviceFactory.getOauthVendorService(oauthVendor);
		
		String result = service.getToken(code);
		TokenInfo ti = new Gson().fromJson(result, TokenInfo.class);

		OauthUserInfo user_info = service.getUserInfo(ti.getAccess_token()); 
		
		User user = User.builder()
				.password(UUID.randomUUID().toString())
				.name(user_info.getNickname())
				.email(SERVICE_PREFIX.replace("{vendor}", oauthVendor.getVendorName().toUpperCase()) + user_info.getEmail())
				.activateMailSendResult(true)
				.activateYn(true)
				.build();
		
		User siteUser;
		if(userService.checkEmail(user.getEmail())) {
			siteUser = userService.getUserByEmail(user.getEmail()).get();
			
		}else {
			siteUser = userService.createUser(user);
		}
		
		sessionUtil.login(req, siteUser.getId());
		
		log.info("received token info is {}", ti);
		return "redirect:" + redirectUrl; 
	}
	
	/**
	 * refresh token
	 * 
	 * @param req
	 * @return
	 */
	@DisableLogin
	@PostMapping(value = "/{oauth-vendor}/token")
	public TokenInfo refreshToken(@PathVariable(name = "oauth-vendor") OauthVendor oauthVendor,String refreshToken) {
		
		OauthServiceIF service = serviceFactory.getOauthVendorService(oauthVendor);
		String result = service.refreshToken(refreshToken);
		TokenInfo tr = new Gson().fromJson(result, TokenInfo.class);
		log.info("received token info is {}", tr);
		return tr; 
	}
	
	
		

}
