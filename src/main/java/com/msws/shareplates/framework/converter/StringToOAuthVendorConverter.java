package com.msws.shareplates.framework.converter;

import java.util.EnumSet;

import org.springframework.core.convert.converter.Converter;

import com.msws.shareplates.biz.oauth.vo.OauthVendor;

public class StringToOAuthVendorConverter implements Converter<String, OauthVendor>{

	@Override
	public OauthVendor convert(String vendorName) {

		if(vendorName == null)
			return OauthVendor.NOT_SUPPORT;
		return EnumSet.allOf(OauthVendor.class).stream().filter(e -> e.getVendorName().equals(vendorName)).findFirst().orElse(OauthVendor.NOT_SUPPORT);
	}

}
