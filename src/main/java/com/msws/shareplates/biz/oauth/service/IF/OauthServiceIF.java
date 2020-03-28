package com.msws.shareplates.biz.oauth.service.IF;

import com.msws.shareplates.biz.oauth.entity.OauthUserInfo;

public interface OauthServiceIF {
	
	public String getToken(String code);
	public String refreshToken(String refresh_token);
	public OauthUserInfo getUserInfo(String token);
}
