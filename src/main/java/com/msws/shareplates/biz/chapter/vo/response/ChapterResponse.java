package com.msws.shareplates.biz.chapter.vo.response;

import java.util.List;

import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.code.AuthCode;
import org.springframework.hateoas.RepresentationModel;

import com.msws.shareplates.biz.chapter.vo.ChapterModel;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChapterResponse extends RepresentationModel<ChapterResponse>{
	
	private List<ChapterModel> chapters;
	private ChapterModel chapter;
	private TopicResponse topic;
	private AuthCode role;

}
