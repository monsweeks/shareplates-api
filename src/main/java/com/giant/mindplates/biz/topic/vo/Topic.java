package com.giant.mindplates.biz.topic.vo;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends RepresentationModel<Topic>{

	private long id;
	private String name;
	private String summary;
	private Integer iconIndex;
	private Boolean privateYn;
}
