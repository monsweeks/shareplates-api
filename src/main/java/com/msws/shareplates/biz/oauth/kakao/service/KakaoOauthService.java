package com.msws.shareplates.biz.oauth.kakao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.msws.shareplates.common.util.HttpRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoOauthService {
	
	private final String HOST = "https://kauth.kakao.com";
	private final String GET_TOKEN_ENDPOINT = "/oauth/token";
	
	
	@Value("${oauth.kakao.client_id}")
    private String client_id;
	
	@Value("${oauth.kakao.redirect_uri}")
	private String redirect_uri;
	
	@Autowired
	private HttpRequestUtil requestUtil;
	
	
	public String getToken(String code) {
		try {
			
			//set token request
			MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<String, String>();
			tokenRequest.add("grant_type", "authorization_code");
			tokenRequest.add("client_id", client_id);
			tokenRequest.add("redirect_uri", redirect_uri);
			tokenRequest.add("code", code);
			
			return requestUtil.sendRequest(HOST + GET_TOKEN_ENDPOINT, tokenRequest , null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
		}catch(Exception e) {
			log.error("fail to get token from kakao server : {}", e);
		}
		
		return null;
		
	}
	
	
	
	
	

}
