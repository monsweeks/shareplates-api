package com.msws.shareplates.biz.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.msws.shareplates.biz.oauth.entity.KakaoOauthUserInfo;
import com.msws.shareplates.biz.oauth.entity.OauthUserInfo;
import com.msws.shareplates.biz.oauth.service.IF.OauthServiceIF;
import com.msws.shareplates.biz.oauth.vo.OauthVendor;
import com.msws.shareplates.common.util.HttpRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoOauthService implements OauthServiceIF{
	
	@Value("${oauth.kakao.host}")
	private String OAUTH_HOST;
	
	@Value("${oauth.kakao.api_host}")
	private String API_HOST;
	
	@Value("${oauth.kakao.token_endpoint}")
	private String GET_TOKEN_ENDPOINT;
	
	@Value("${oauth.kakao.userinfo_endpoint}")
	private String GET_USERINFO_ENDPOINT;
	
	private final String AUTHORIZATION = "Bearer ";
		
	
	@Value("${oauth.kakao.client_id}")
    private String client_id;
	
	@Value("${oauth.kakao.redirect_uri}")
	private String redirect_uri;
	
	@Autowired
	private HttpRequestUtil requestUtil;
	
	
	/**
	 * get oauth token
	 * 
	 * @param code
	 * @return
	 */
	public String getToken(String code) {
		try {
			
			//set token request
			MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<String, String>();
			tokenRequest.add("grant_type", "authorization_code");
			tokenRequest.add("client_id", client_id);
			tokenRequest.add("redirect_uri", redirect_uri);
			tokenRequest.add("code", code);
			
			return requestUtil.sendRequest(OAUTH_HOST + GET_TOKEN_ENDPOINT, tokenRequest , null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
		}catch(Exception e) {
			log.error("fail to get token from kakao server : {}", e);
		}
		
		return null;
		
	}
	
	
	/**
	 * refresh token
	 * 
	 * @param code
	 * @return
	 */
	public String refreshToken(String refresh_token) {
		try {
			
			//set token request
			MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<String, String>();
			tokenRequest.add("grant_type", "refresh_token");
			tokenRequest.add("client_id", client_id);
			tokenRequest.add("refresh_token", refresh_token);
			
			return requestUtil.sendRequest(OAUTH_HOST + GET_TOKEN_ENDPOINT, tokenRequest , null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
		}catch(Exception e) {
			log.error("fail to get token from kakao server : {}", e);
		}
		
		return null;
		
	}
	
	public OauthUserInfo getUserInfo(String token) {
		
		log.info("token is {}", token);
		try {
			String[] headers = { "Authorization" };
			String[] values = { AUTHORIZATION + token};
			String api_result = requestUtil.sendRequest(API_HOST + GET_USERINFO_ENDPOINT, null , headers, values, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
			KakaoOauthUserInfo result = new Gson().fromJson(api_result , KakaoOauthUserInfo.class);
			return new OauthUserInfo(result.getId(), 
									 result.getProperties().getNickname(), 
									 result.getKakao_account().getEmail());
		}catch(Exception e) {
			log.error("fail to get token from kakao server : {}", e);
		}
		
		return null;
		
	}

	public OauthVendor getVendor() {
		return OauthVendor.kakao;
	}

}

