package com.msws.shareplates.biz.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.msws.shareplates.biz.oauth.entity.FacebookOauthUserInfo;
import com.msws.shareplates.biz.oauth.entity.KakaoOauthUserInfo;
import com.msws.shareplates.biz.oauth.entity.OauthUserInfo;
import com.msws.shareplates.biz.oauth.service.IF.OauthServiceIF;
import com.msws.shareplates.biz.oauth.service.annotation.VendorType;
import com.msws.shareplates.biz.oauth.vo.OauthVendor;
import com.msws.shareplates.common.exception.VendorException;
import com.msws.shareplates.common.exception.code.VendorExceptionCode;
import com.msws.shareplates.common.util.HttpRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@VendorType(vendor=OauthVendor.FACEBOOK)
public class FacebookOauthService implements OauthServiceIF{
	
	@Value("${oauth.facebook.host}")
	private String oauthHost;
	
	@Value("${oauth.facebook.api-host}")
	private String apiHost;
	
	@Value("${oauth.facebook.token-endpoint}")
	private String getTokenEndpoint;
	
	@Value("${oauth.facebook.userinfo-endpoint}")
	private String getUserinfoEndpoint;
	
	private final String AUTHORIZATION = "Bearer ";
		
	
	@Value("${oauth.facebook.client-id}")
    private String clientId;
	
	@Value("${oauth.facebook.client-secret}")
    private String clientSecret;
	
	@Value("${oauth.facebook.redirect-uri}")
	private String redirectUri;
	
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
			tokenRequest.add("client_id", clientId);
			tokenRequest.add("client_secret", clientSecret);
			tokenRequest.add("redirect_uri", redirectUri);
			tokenRequest.add("code", code);
			
			return requestUtil.sendRequest(oauthHost + getTokenEndpoint, tokenRequest , null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
		}catch(Exception e) {
			log.error("fail to get token from kakao server : {}", e);
			
			throw new VendorException(VendorExceptionCode.FACEBOOK_OAUTH2_SERVICE_NOT_AVAILABLE);
		}
		
	}
	
	
	public OauthUserInfo getUserInfo(String token) {
		
		log.info("token is {}", token);
		try {
			
			
			//set token request
			MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<String, String>();
			tokenRequest.add("fields", "email");
			tokenRequest.add("access_token", token);
			
			String api_result = requestUtil.sendRequest(apiHost + getUserinfoEndpoint, tokenRequest , null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
			log.info("Facebook user info data : {}", api_result);
			FacebookOauthUserInfo result = new Gson().fromJson(api_result , FacebookOauthUserInfo.class);
			
			return new OauthUserInfo(result.getId(), 
									 result.getEmail().split("@")[0], 
									 result.getEmail());
		}catch(Exception e) {
			log.error("fail to get token from Facebook server : {}", e);
			
			throw new VendorException(VendorExceptionCode.FACEBOOK_OAUTH2_SERVICE_NOT_AVAILABLE);
		}
		
	}


	@Override
	public String refreshToken(String refresh_token) {
		// TODO Auto-generated method stub
		return null;
	}

}

