package com.msws.shareplates.common.vo;

import com.msws.shareplates.biz.chapter.vo.response.ChapterResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
public class EmptyResponse extends RepresentationModel<ChapterResponse> {
	
	private static EmptyResponse emptyResponse = new EmptyResponse();
	
	public static EmptyResponse getInstance() {
		return emptyResponse;
	}

}
