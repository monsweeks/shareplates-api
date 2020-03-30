package com.msws.shareplates.biz.chapter.vo.request;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.topic.entity.Topic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChapterRequest {

	@ApiModelProperty(value="제목", example = "챕터1")
	private String title;
	@ApiModelProperty(value="요약", example = "맛있게 먹는법")
	private String summary;
	@ApiModelProperty(value="정렬순서", example = "1")
	private int orderNo;
	@ApiModelProperty(value="사용여부", example = "true")
	private boolean useYn;
	@ApiModelProperty(value="토픽 ID", example = "11")
	private long topicId;
	@ApiModelProperty(value="챕터 ID", example = "1")
	private long id;
	
	public Chapter buildChaterEntity() {
		return Chapter.builder()
				.id(id)
				.orderNo(orderNo)
				.title(title)
				.summary(summary)
				.useYn(useYn)
				.topic(Topic.builder().id(topicId).build())
				.build();
	}
	
	public Chapter buildChaterEntity(long chapterId) {
		return Chapter.builder()
				.id(chapterId)
				.orderNo(orderNo)
				.title(title)
				.summary(summary)
				.useYn(useYn)
				.topic(Topic.builder().id(topicId).build())
				.build();
	}
}
