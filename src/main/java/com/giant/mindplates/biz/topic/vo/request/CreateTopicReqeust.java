package com.giant.mindplates.biz.topic.vo.request;

import java.util.List;

import lombok.Data;

@Data
public class CreateTopicReqeust {
	
	private String name;
	private String summary;
	private long organizationId;
	private int iconIndex;
	private boolean privateYn;
	
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
