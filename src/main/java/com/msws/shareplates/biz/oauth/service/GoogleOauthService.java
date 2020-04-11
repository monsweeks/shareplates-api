package com.msws.shareplates.biz.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.msws.shareplates.biz.oauth.entity.GoogleOauthUserInfo;
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
@VendorType(vendor=OauthVendor.GOOGLE)
public class GoogleOauthService implements OauthServiceIF{
	
	@Value("${oauth.google.host}")
	private String oauthHost;
	
	@Value("${oauth.google.api-host}")
	private String apiHost;
	
	@Value("${oauth.google.token-endpoint}")
	private String getTokenEndpoint;
	
	@Value("${oauth.google.userinfo-endpoint}")
	private String getUserinfoEndpoint;
	
	private final String AUTHORIZATION = "Bearer ";
		
	
	@Value("${oauth.google.client-id}")
    private String clientId;
	
	@Value("${oauth.google.client-secret}")
    private String clientSecret;
	
	@Value("${oauth.google.redirect-uri}")
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
			
			String result = requestUtil.sendRequest(oauthHost + getTokenEndpoint, tokenRequest , null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED); 
			return result;
		}catch(Exception e) {
			log.error("fail to get token from kakao server : {}", e);
			
			throw new VendorException(VendorExceptionCode.GOOGLE_OAUTH2_SERVICE_NOT_AVAILABLE);
		}
		
	}
	
	
	public OauthUserInfo getUserInfo(String token) {
		
		log.info("token is {}", token);
		try {
			
			String api_result = requestUtil.sendRequest(apiHost + getUserinfoEndpoint + "?access_token=" + token, null , null, null, HttpMethod.GET, MediaType.APPLICATION_FORM_URLENCODED);
			log.info("Google user info data : {}", api_result);
			GoogleOauthUserInfo result = new Gson().fromJson(api_result , GoogleOauthUserInfo.class);
			
			return new OauthUserInfo(result.getId(), 
									 result.getEmail().split("@")[0], 
									 result.getEmail());
		}catch(Exception e) {
			log.error("fail to get token from Google server : {}", e);
			
			throw new VendorException(VendorExceptionCode.GOOGLE_OAUTH2_SERVICE_NOT_AVAILABLE);
		}
		
	}
	
	@Override
	public String refreshToken(String refresh_token) {
		// TODO Auto-generated method stub
		return null;
	}

}

