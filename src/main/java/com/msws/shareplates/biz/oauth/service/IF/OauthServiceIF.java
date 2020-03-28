package com.msws.shareplates.biz.oauth.service.IF;

import com.msws.shareplates.biz.oauth.entity.OauthUserInfo;
import com.msws.shareplates.biz.oauth.vo.OauthVendor;

public interface OauthServiceIF {
	
	public OauthVendor getVendor();
	public String getToken(String code);
	public String refreshToken(String refresh_token);
	public OauthUserInfo getUserInfo(String token);
}
