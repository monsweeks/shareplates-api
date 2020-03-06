package com.giant.mindplates.biz.topic.vo.response;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetTopicsResponse extends RepresentationModel<GetTopicsResponse> {

	private List<Topic> topics;
	
	@Builder
	@Data
	public static class Topic extends RepresentationModel<Topic>{
		private long id;
		private String name;
		private String summary;
	}
}
