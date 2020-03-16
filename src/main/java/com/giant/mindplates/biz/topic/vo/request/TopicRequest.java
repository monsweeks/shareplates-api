package com.giant.mindplates.biz.topic.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class TopicRequest {

	private Long id;
	private String name;
	private String summary;
	private Long organizationId;
	private Integer iconIndex;
	private Boolean privateYn;
	private List<User> users;
	
	@Data
	public static class User{
		private Long id;
		private String email;
		private String name;
		private Long organizationId;
		private String info;
	}

}
