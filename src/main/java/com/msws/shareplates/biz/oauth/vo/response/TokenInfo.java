package com.msws.shareplates.biz.oauth.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
	
	private String access_token;
	private String token_type;
	private String refresh_token;
	private String scope;
	private String id_token;
	
	@Override
	public String toString() {
		
		return "access_token = " + access_token + ", refresh_token = " + refresh_token; 
	}

}
