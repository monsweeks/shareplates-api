package com.msws.shareplates.framework.session.vo;

import java.io.Serializable;

import com.msws.shareplates.common.code.RoleCode;

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

	public static class Grp {
		private long oranizationId;
		private RoleCode auth;
	}
}
