package com.giant.mindplates.biz.topic.vo.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

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
		private Integer iconIndex;
		private Boolean privateYn;
	}
}
