package com.msws.shareplates.biz.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OauthUserInfo {
	
	private Double id;
	private String nickname;
	private String email;

}
