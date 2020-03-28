package com.msws.shareplates.biz.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoOauthUserInfo{
	
		
	private Long id;
	private Account_property properties;
	private Account_info kakao_account;
	

	
	@Getter
	@Setter
	public class Account_property{
		String nickname;		
	}
	
	@Getter
	@Setter
	public class Account_info{
		String email;		
		
	}
}
