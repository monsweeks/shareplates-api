package com.msws.shareplates.biz.topic.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class TopicRequest {

	private Long id;
	private String name;
	private String summary;
	private Long grpId;
	private Integer iconIndex;
	private Boolean privateYn;
	private String content;
	private List<User> users;
	
	@Data
	public static class User{
		private Long id;
		private String email;
		private String name;
		private Long grpId;
		private String info;
	}

}
