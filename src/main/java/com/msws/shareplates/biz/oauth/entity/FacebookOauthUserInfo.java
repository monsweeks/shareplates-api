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
public class FacebookOauthUserInfo{
	
	private Double id;
	private String email;
	
}
