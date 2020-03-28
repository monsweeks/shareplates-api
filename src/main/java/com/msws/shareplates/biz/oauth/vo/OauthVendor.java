package com.msws.shareplates.biz.oauth.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OauthVendor {
		
		kakao("kakao");
		
		private String vendor_name;

}
