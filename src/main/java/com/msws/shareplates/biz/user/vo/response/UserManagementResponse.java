package com.msws.shareplates.biz.user.vo.response;

import java.util.List;

import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	    private RoleCode shareRoleCode;
	    private SocketStatusCode status;
	    private String message;
	    private Boolean banYn;
	}
}
