package com.msws.shareplates.biz.chapter.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.chapter.vo.request.ChapterOrdersRequest;
import com.msws.shareplates.biz.chapter.vo.request.ChapterRequest;
import com.msws.shareplates.biz.chapter.vo.response.ChapterResponse;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.session.vo.UserInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private TopicService topicService;

    @ApiOperation(value = "챕터 저장")
    @PutMapping("")
    public ChapterResponse insertChapter(@RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
        Chapter chapter = chapterService.saveChapter(chapterRequest.buildChaterEntity(), userInfo);
        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter)
                                .add(linkTo(methodOn(ChapterController.class).getChapter(chapter.getId(), chapterRequest, userInfo)).withSelfRel())
                                .add(linkTo(methodOn(ChapterController.class).updateChapter(chapter.getId(), chapterRequest, userInfo)).withRel("update-chapter"))
                        // .add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter"))
                )
                .build();
    }

    @ApiOperation(value = "챕터 수정")
    @PostMapping("/{chapter-id}")
    public ChapterResponse updateChapter(@PathVariable("chapter-id") long charpterId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
        Chapter chapter = chapterService.saveChapter(chapterRequest.buildChaterEntity(charpterId), userInfo);
        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter)
                                .add(linkTo(methodOn(ChapterController.class).getChapter(chapter.getId(), chapterRequest, userInfo)).withSelfRel())
                                .add(linkTo(methodOn(ChapterController.class).updateChapter(chapter.getId(), chapterRequest, userInfo)).withRel("update-chapter"))
                        // .add(linkTo(methodOn(ChapterApiController.class).deleteChapter(chapterRequest, userInfo)).withRel("delete-chapter"))
                )
                .build();
    }

    @ApiOperation(value = "챕터 제목 수정")
    @PostMapping("/{chapter-id}/title")
    public ChapterResponse updateChapterTitle(@PathVariable("chapter-id") long chapterId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {

        Chapter chapter = chapterService.getChapter(chapterId, userInfo);
        chapter.setTitle(chapterRequest.getTitle());
        chapterService.saveChapter(chapter, userInfo);

        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 순서 변경")
    @PutMapping("/orders")
    public EmptyResponse updateChapterOrders(@RequestBody ChapterOrdersRequest chapterOrdersRequest, UserInfo userInfo) {
        // TODO 현재 사용자의 토픽에 대한 쓰기 권한 체크 필요
        chapterService.updateChapterOrders(chapterOrdersRequest.getTopicId(), chapterOrdersRequest.buildChaterListEntity());
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "챕터 삭제")
    @DeleteMapping("/{chapter-id}")
    public EmptyResponse deleteChapter(@PathVariable("chapter-id") long chapterId, UserInfo userInfo) {
        Chapter chapter = chapterService.getChapter(chapterId, userInfo);
        chapterService.deleteChapter(chapter, userInfo);
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "챕터 목록")
    @GetMapping("")
    public ChapterResponse getChapters(ChapterRequest chapterRequest, UserInfo userInfo) {

        String role = topicService.selectUserTopicRole(chapterRequest.getTopicId(), userInfo.getId());

        return ChapterResponse.builder()
                .chapters(chapterService.getChapters(chapterRequest.buildChaterEntity(), userInfo).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .topic(new TopicResponse(topicService.selectTopic(chapterRequest.getTopicId())))
                .role(role)
                .build();
    }

    @ApiOperation(value = "챕터 정보")
    @GetMapping("/{chapter-id}")
    public ChapterResponse getChapter(@PathVariable("chapter-id") long chapterId, ChapterRequest chapterRequest, UserInfo userInfo) {

        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapterService.getChapter(chapterId, chapterRequest.buildChaterEntity(), userInfo)))
                .build();
    }
}
