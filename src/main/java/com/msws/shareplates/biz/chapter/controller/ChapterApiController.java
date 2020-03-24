package com.msws.shareplates.biz.chapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.request.ChapterRequest;
import com.msws.shareplates.framework.session.vo.UserInfo;

@RestController
@RequestMapping("/api/chapter")
public class ChapterApiController {
	
	@Autowired
	private ChapterService chapterService;

	@PutMapping("")
	public void createChapter(@RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
		chapterService.createChater(chapterRequest.buildChaterEntity(), userInfo);
	}
}
