package com.msws.shareplates.biz.chapter.controller;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.chapter.vo.request.ChapterRequest;
import com.msws.shareplates.biz.chapter.vo.response.ChapterResponse;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/chapters")
public class ChapterApiController {
	
	@Autowired
	private ChapterService chapterService;

	@Autowired
	private TopicService topicService;

	@ApiOperation(value = "챕터 저장/수정")
	@PostMapping("")
	public ChapterResponse saveChapter(@RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
		Chapter chapter = chapterService.saveChapter(chapterRequest.buildChaterEntity(), userInfo);
		return ChapterResponse.builder()
				.chapter(ChapterModel.builder().build().buildChapterModel(chapter)
								.add(linkTo(methodOn(ChapterApiController.class).getChapter(chapter.getId(), chapterRequest, userInfo)).withSelfRel())
								.add(linkTo(methodOn(ChapterApiController.class).saveChapter(chapterRequest, userInfo)).withRel("update-chapter"))
						// .add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter"))
				)
				.build();
	}
	
	@ApiOperation(value = "챕터 삭제")
	@DeleteMapping("/{chapter-id}")
	public EmptyResponse deleteChapter(@PathVariable("chapter-id") long chapterId,UserInfo userInfo) {
		Chapter chapter = chapterService.getChapter(chapterId, userInfo);
		chapterService.deleteChapter(chapter, userInfo);
		return EmptyResponse.getInstance();
	}
	
	@ApiOperation(value = "챕터 목록")
	@GetMapping("")
	public ChapterResponse getChapters(ChapterRequest chapterRequest, UserInfo userInfo) {
		return ChapterResponse.builder()
				.chapters(chapterService.getChapters(chapterRequest.buildChaterEntity(), userInfo).stream()
						.map(chapter 
								-> ChapterModel.builder().build().buildChapterModel(chapter)
								.add(linkTo(methodOn(ChapterApiController.class).getChapter(chapter.getId(), chapterRequest, userInfo)).withSelfRel())
								.add(linkTo(methodOn(ChapterApiController.class).saveChapter(chapterRequest, userInfo)).withRel("update-chapter"))
								// .add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter"))
						)
						.collect(Collectors.toList()))
				.topic(new TopicResponse(topicService.selectTopic(chapterRequest.getTopicId())))
				.build();
	}
	
	@ApiOperation(value = "챕터 정보")
	@GetMapping("/{chapter-id}")
	public ChapterResponse getChapter(@PathVariable("chapter-id") long chapterId, ChapterRequest chapterRequest, UserInfo userInfo) {
		
		return ChapterResponse.builder()
				.chapter(ChapterModel.builder().build().buildChapterModel(chapterService.getChapter(chapterId, chapterRequest.buildChaterEntity(), userInfo))
						.add(linkTo(methodOn(ChapterApiController.class).getChapter(chapterId, chapterRequest, userInfo)).withSelfRel())
						.add(linkTo(methodOn(ChapterApiController.class).saveChapter(chapterRequest, userInfo)).withRel("update-chapter"))
						// .add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter"))
				)
				.build();
	}
}
