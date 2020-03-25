package com.msws.shareplates.biz.chapter.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.chapter.vo.request.ChapterRequest;
import com.msws.shareplates.biz.chapter.vo.response.ChapterResponse;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.session.vo.UserInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/chapter")
public class ChapterApiController {
	
	@Autowired
	private ChapterService chapterService;

	@ApiOperation(value = "챕터 저장/수정")
	@PostMapping("")
	public EmptyResponse saveChapter(@RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
		chapterService.saveChater(chapterRequest.buildChaterEntity(), userInfo);
		
		return EmptyResponse.getInstance();
	}
	
	@ApiOperation(value = "챕터 삭제")
	@DeleteMapping("")
	public EmptyResponse deleteChapter(@RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
		chapterService.deleteChapter(chapterRequest.buildChaterEntity(), userInfo);
		
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
								.add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter")))
						.collect(Collectors.toList()))
				.build();
	}
	
	@ApiOperation(value = "챕터 정보")
	@GetMapping("/{chapter-id}")
	public ChapterResponse getChapter(@PathVariable("chapter-id") long chapterId, ChapterRequest chapterRequest, UserInfo userInfo) {
		
		return ChapterResponse.builder()
				.chapter(ChapterModel.builder().build().buildChapterModel(chapterService.getChapter(chapterId, chapterRequest.buildChaterEntity(), userInfo))
						.add(linkTo(methodOn(ChapterApiController.class).getChapter(chapterId, chapterRequest, userInfo)).withSelfRel())
						.add(linkTo(methodOn(ChapterApiController.class).saveChapter(chapterRequest, userInfo)).withRel("update-chapter"))
						.add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter")))
				.build();
	}
}
