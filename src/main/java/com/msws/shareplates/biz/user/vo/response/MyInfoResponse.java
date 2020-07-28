package com.msws.shareplates.biz.user.vo.response;

import com.msws.shareplates.common.code.RoleCode;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyInfoResponse {
	
	private long shareCount;
	private UserInfo user;
	private List<GroupInfo> grps;
	
	@Builder
	@Getter
	public static class UserInfo{
		private long id;
		private String email;
		private String name;
		private String info;
		private RoleCode roleCode;
		private RoleCode activeRoleCode;
		private String language;
		private String dateTimeFormat;
	}
	
	@Builder
	@Getter
	public static class GroupInfo{
		private long id;
		private String name;
		private boolean publicYn;

	}
	

}
