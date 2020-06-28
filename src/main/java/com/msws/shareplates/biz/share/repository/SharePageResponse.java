package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.page.entity.Page;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
public class SharePageResponse extends RepresentationModel<SharePageResponse>{

	private Long id;
	private String title;
	private String content;
	private int orderNo;
	private long chapterId;
	
	public SharePageResponse buildChapterModel(Page page) {
		
		this.id = page.getId();
		this.title = page.getTitle();
		this.content = page.getContent();
		this.orderNo = page.getOrderNo();
		this.chapterId = page.getChapter().getId();
		
		return this;
	}
}
