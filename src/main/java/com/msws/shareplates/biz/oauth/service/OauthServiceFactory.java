package com.msws.shareplates.biz.oauth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.oauth.service.IF.OauthServiceIF;
import com.msws.shareplates.biz.oauth.service.annotation.VendorType;
import com.msws.shareplates.biz.oauth.vo.OauthVendor;
import com.msws.shareplates.common.exception.VendorException;
import com.msws.shareplates.common.exception.code.VendorExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OauthServiceFactory{
	
	@Autowired
	private List<OauthServiceIF> service;
	
	public OauthServiceIF getOauthVendorService(OauthVendor vendorName) {
		
		log.info("received oauth vendor name {}", vendorName);
		return service.stream().filter(e -> e.getClass().getAnnotation(VendorType.class).vendor() == vendorName)
				.findFirst()
				.orElseThrow(() -> new VendorException(VendorExceptionCode.KAKAO_OAUTH2_SERVICE_NOT_AVAILABLE));
		
	}
	


}

