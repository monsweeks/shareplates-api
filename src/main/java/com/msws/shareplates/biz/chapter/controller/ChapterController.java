package com.msws.shareplates.biz.chapter.controller;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.chapter.vo.request.ChapterOrdersRequest;
import com.msws.shareplates.biz.chapter.vo.request.ChapterRequest;
import com.msws.shareplates.biz.chapter.vo.response.ChapterResponse;
import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics/{topic-id}/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "챕터 생성")
    @PostMapping("")
    public ChapterResponse createChapter(@PathVariable(value = "topic-id") long topicId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {

        Chapter chapter = chapterService.createChapter(chapterRequest.buildChaterEntity());
        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 수정")
    @PutMapping("/{chapterId}")
    public ChapterResponse updateChapter(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapterId") long chapterId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
       
        Chapter chapter = chapterService.updateChapter(chapterRequest.buildChaterEntity());
        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 제목 수정")
    @PutMapping("/{chapterId}/title")
    public ChapterResponse updateChapterTitle(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapterId") long chapterId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
        
    	Chapter chapter = chapterService.selectChapter(chapterId, topicId);
        chapterService.updateChapter(chapter.setTitle(chapterRequest.getTitle()));

        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 순서 변경")
    @PutMapping("/orders")
    public EmptyResponse updateChapterOrders(@PathVariable(value = "topic-id") long topicId, @RequestBody ChapterOrdersRequest chapterOrdersRequest, UserInfo userInfo) {
        
        chapterService.updateChapterOrders(chapterOrdersRequest.getTopicId(), chapterOrdersRequest.buildChaterListEntity());
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "챕터 삭제")
    @DeleteMapping("/{chapterId}")
    public TopicResponse deleteChapter(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapterId") long chapterId, UserInfo userInfo) {
        Chapter chapter = chapterService.selectChapter(chapterId, topicId);
        chapterService.deleteChapter(chapter);
        Topic topic = topicService.selectTopic(chapter.getTopic().getId());
        return new TopicResponse(topic);
    }

    @ApiOperation(value = "챕터 목록")
    @GetMapping("")
    public ChapterResponse selectChapters(@PathVariable(value = "topic-id") long topicId, ChapterRequest chapterRequest, UserInfo userInfo) {
        AuthCode role = topicService.selectUserTopicRole(chapterRequest.getTopicId(), userInfo.getId());

        return ChapterResponse.builder()
                .chapters(chapterService.selectChapters(chapterRequest.buildChaterEntity()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .topic(new TopicResponse(topicService.selectTopic(chapterRequest.getTopicId())))
                .role(role)
                .build();
    }

    @ApiOperation(value = "챕터 정보")
    @GetMapping("/{chapterId}")
    public ChapterResponse selectChapter(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapterId") long chapterId, UserInfo userInfo) {

        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapterService.selectChapter(chapterId, topicId)))
                .build();
    }
}
