package com.giant.mindplates.framework.session.vo;

import java.io.Serializable;

import com.giant.mindplates.common.code.AuthCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable{
	
	private long id;
	private AuthCode auth;

}
