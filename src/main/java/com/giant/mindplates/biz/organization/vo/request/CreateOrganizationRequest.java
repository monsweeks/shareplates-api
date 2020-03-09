package com.giant.mindplates.biz.organization.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrganizationRequest {
	
	private String name;
	private String description;
	
	private List<User> admins;
	private List<User> members;
	
	@Data
	public static class User{
		private long id;
	}

}
