package com.msws.shareplates.biz.chapter.vo.request;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.topic.entity.Topic;

import lombok.Data;

@Data
public class ChapterRequest {

	private String title;
	private String summary;
	private int orderNo;
	private boolean useYn;
	private long topicId;
	
	public Chapter buildChaterEntity() {
		return Chapter.builder()
				.orderNo(orderNo)
				.title(title)
				.summary(summary)
				.useYn(useYn)
				.topic(Topic.builder().id(topicId).build())
				.build();
	}
}
