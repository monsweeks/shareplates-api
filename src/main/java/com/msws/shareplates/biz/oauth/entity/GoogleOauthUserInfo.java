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
public class GoogleOauthUserInfo{
	
	private double id;
	private String email;
	private String picture;
	private boolean verified_email;
	
}
