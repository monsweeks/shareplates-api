package com.msws.shareplates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.msws.shareplates.biz.oauth.kakao.service.KakaoOauthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class OauthLoginTest {
	
	@Autowired
	private KakaoOauthService kakaoservice;
	
	@Test
	public void test() {
		
		try {
			
			String code = "";
			String result = kakaoservice.getToken(code);
			log.debug("Code from kakao is {}", result);
			
			
		} catch (Exception e) {
			log.error("fail to login using KAKAO Oauth 2.0 : {}", e);
		}
		
	}

}
