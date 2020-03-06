package com.giant.mindplates.biz.topic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicUserId implements Serializable{

    @Column(name = "user_id")
	private long userId;	
    @Column(name = "topic_id")
	private long topicId;
}
