package com.giant.mindplates.biz.topic.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.giant.mindplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "topic_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopicUser extends CommonEntity{	

	@EmbeddedId
	private TopicUserId topicUserId;

	@MapsId("topicId")
	@ManyToOne
	@JoinColumn(name="topic_id", referencedColumnName = "id")
	private Topic topic;	
}
