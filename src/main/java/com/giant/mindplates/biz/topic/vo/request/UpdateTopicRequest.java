package com.giant.mindplates.biz.topic.vo.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateTopicRequest {
	
	private String name;
	private String summary;
	private long organizationId;
	private int iconIndex;
	private boolean privateYn;
	private long id;
	
	private List<User> users;
	
	@Data
	public static class User{
		private long id;
		private String email;
		private String name;
		private long organizationId;
		private String info;
	}

}
