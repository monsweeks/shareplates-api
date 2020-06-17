package com.msws.shareplates.biz.user.vo.response;

import com.msws.shareplates.common.code.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserManagementResponse {

	private List<User> userList;
	private User user;
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class User{
	    private Long id;
	    private String email;
	    private String name;
	    private String info;
		private String dateTimeFormat;
		private String language;
		private Boolean registered;
		private RoleCode roleCode;
		private RoleCode activeRoleCode;
	}
}
