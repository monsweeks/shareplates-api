package com.msws.shareplates.biz.oauth.vo;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OauthVendor {
		
	KAKAO("kakao"),
	NOT_SUPPORT("not_support");
	
	private String vendorName;
		
	@JsonCreator
	public static OauthVendor getVendor(String vendorName) {
		if(vendorName == null)
			return OauthVendor.NOT_SUPPORT;
		return EnumSet.allOf(OauthVendor.class).stream().filter(e -> e.getVendorName().equals(vendorName)).findFirst().orElse(OauthVendor.NOT_SUPPORT);
	}

}
