package com.msws.shareplates.biz.oauth.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.msws.shareplates.biz.oauth.vo.OauthVendor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VendorType {

	OauthVendor vendor();
}
