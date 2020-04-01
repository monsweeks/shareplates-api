package com.msws.shareplates.biz.page.vo.request;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.page.entity.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest {

	@ApiModelProperty(value="페이지 ID", example = "1")
	private long id;

	@ApiModelProperty(value="제목", example = "페이지1")
	private String title;

	@ApiModelProperty(value="컨텐츠", example = "페이지 컨텐츠")
	private String content;

	@ApiModelProperty(value="정렬순서", example = "1")
	private int orderNo;

	@ApiModelProperty(value="챕터 ID", example = "11")
	private long chapterId;

	
	public Page buildPageEntity() {
		return Page.builder()
				.id(id)
				.title(title)
				.content(content)
				.orderNo(orderNo)
				.chapter(Chapter.builder().id(chapterId).build())
				.build();
	}
}
