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
public class NaverOauthUserInfo{
	
	private Account_property response;
	
	
	
	@Getter
	@Setter
	public class Account_property{
		Double id;
		String email;
	}
	
	
}
