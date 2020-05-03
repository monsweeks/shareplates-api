package com.msws.shareplates.biz.chapter.vo;

import org.springframework.hateoas.RepresentationModel;

import com.msws.shareplates.biz.chapter.entity.Chapter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChapterModel extends RepresentationModel<ChapterModel>{

	private String title;
	private String summary;
	private int orderNo;
	private boolean useYn;
	private long topicId;
	private long id;
	private int pageCount;
	
	public ChapterModel buildChapterModel(Chapter chapter) {
		
		title = chapter.getTitle();
		summary = chapter.getSummary();
		orderNo = chapter.getOrderNo();
		useYn = chapter.getUseYn();
		topicId = chapter.getTopic().getId();
		id = chapter.getId();
		pageCount = chapter.getPageCount() != null ? chapter.getPageCount() : 0;
		
		return this;
	}
}
