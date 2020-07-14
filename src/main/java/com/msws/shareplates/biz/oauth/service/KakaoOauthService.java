package com.msws.shareplates.biz.oauth.service;

import com.google.gson.Gson;
import com.msws.shareplates.biz.oauth.entity.KakaoOauthUserInfo;
import com.msws.shareplates.biz.oauth.entity.OauthUserInfo;
import com.msws.shareplates.biz.oauth.service.IF.OauthServiceIF;
import com.msws.shareplates.biz.oauth.service.annotation.VendorType;
import com.msws.shareplates.biz.oauth.vo.OauthVendor;
import com.msws.shareplates.common.exception.VendorException;
import com.msws.shareplates.common.exception.code.VendorExceptionCode;
import com.msws.shareplates.common.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
@VendorType(vendor = OauthVendor.KAKAO)
public class KakaoOauthService implements OauthServiceIF {

    private final String AUTHORIZATION = "Bearer ";
    @Value("${oauth.kakao.host}")
    private String oauthHost;
    @Value("${oauth.kakao.api-host}")
    private String apiHost;
    @Value("${oauth.kakao.token-endpoint}")
    private String getTokenEndpoint;
    @Value("${oauth.kakao.userinfo-endpoint}")
    private String getUserinfoEndpoint;
    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private HttpRequestUtil requestUtil;


    public String getToken(String code) {
        try {

            //set token request
            MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<String, String>();
            tokenRequest.add("grant_type", "authorization_code");
            tokenRequest.add("client_id", clientId);
            tokenRequest.add("redirect_uri", redirectUri);
            tokenRequest.add("code", code);

            return requestUtil.sendRequest(oauthHost + getTokenEndpoint, tokenRequest, null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
        } catch (Exception e) {
            log.error("fail to get token from kakao server : {}", e);

            throw new VendorException(VendorExceptionCode.KAKAO_OAUTH2_SERVICE_NOT_AVAILABLE);
        }

    }


    public String refreshToken(String refresh_token) {
        try {

            //set token request
            MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<String, String>();
            tokenRequest.add("grant_type", "refresh_token");
            tokenRequest.add("client_id", clientId);
            tokenRequest.add("refresh_token", refresh_token);

            return requestUtil.sendRequest(oauthHost + getTokenEndpoint, tokenRequest, null, null, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
        } catch (Exception e) {
            log.error("fail to get token from kakao server : {}", e);

            throw new VendorException(VendorExceptionCode.KAKAO_OAUTH2_SERVICE_NOT_AVAILABLE);
        }

    }

    public OauthUserInfo getUserInfo(String token) {

        log.info("token is {}", token);
        try {
            String[] headers = {"Authorization"};
            String[] values = {AUTHORIZATION + token};
            String api_result = requestUtil.sendRequest(apiHost + getUserinfoEndpoint, null, headers, values, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
            KakaoOauthUserInfo result = new Gson().fromJson(api_result, KakaoOauthUserInfo.class);

            
            log.info("user info : id -> {}  nickname -> {}  email -> {}", result.getId(), result.getProperties().getNickname(),result.getKakao_account().getEmail());
            return new OauthUserInfo(result.getId(),
                    result.getProperties().getNickname(),
                    result.getKakao_account().getEmail());
        } catch (Exception e) {
            log.error("fail to get token from kakao server : {}", e);

            throw new VendorException(VendorExceptionCode.KAKAO_OAUTH2_SERVICE_NOT_AVAILABLE);
        }

    }

}

