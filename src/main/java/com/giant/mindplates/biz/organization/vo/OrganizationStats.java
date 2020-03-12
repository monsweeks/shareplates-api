package com.giant.mindplates.biz.organization.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationStats extends RepresentationModel<OrganizationStats>{

	private Long id;
	private String name;
	private String description;
	private Boolean publicYn;
	private LocalDateTime creationDate;
	private Long userCount;
	private Long topicCount;
	private String role;
}
