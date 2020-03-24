package com.msws.shareplates.biz.oauth.kakao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.oauth.kakao.service.KakaoOauthService;
import com.msws.shareplates.framework.annotation.DisableLogin;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequestMapping("oauth/kakao")
@RestController
public class KakaoOauthController {
	
	@Autowired
	private KakaoOauthService service;
	
	

	@DisableLogin
	@GetMapping(value = "/login")
	public String getAuthorizationCode(String code) {
		log.info("received authorization code is {}", code);
		return service.getToken(code);
	}
	
	

}
