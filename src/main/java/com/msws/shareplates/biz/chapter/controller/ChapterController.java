package com.msws.shareplates.biz.chapter.controller;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.chapter.vo.request.ChapterOrdersRequest;
import com.msws.shareplates.biz.chapter.vo.request.ChapterRequest;
import com.msws.shareplates.biz.chapter.vo.response.ChapterResponse;
import com.msws.shareplates.biz.common.service.AuthService;
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
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "챕터 생성")
    @PostMapping("")
    public ChapterResponse createChapter(@RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(chapterRequest.getTopicId(), userInfo.getId());

        Chapter chapter = chapterService.createChapter(chapterRequest.buildChaterEntity());
        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 수정")
    @PutMapping("/{chapterId}")
    public ChapterResponse updateChapter(@PathVariable("chapterId") long chapterId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(chapterRequest.getTopicId(), userInfo.getId());

        Chapter chapter = chapterService.updateChapter(chapterRequest.buildChaterEntity(chapterId));
        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 제목 수정")
    @PutMapping("/{chapterId}/title")
    public ChapterResponse updateChapterTitle(@PathVariable("chapterId") long chapterId, @RequestBody ChapterRequest chapterRequest, UserInfo userInfo) {
        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(chapterRequest.getTopicId(), userInfo.getId());

        Chapter chapter = chapterService.getChapter(chapterId);
        chapterService.createChapter(chapter.setTitle(chapterRequest.getTitle()));

        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .build();
    }

    @ApiOperation(value = "챕터 순서 변경")
    @PutMapping("/orders")
    public EmptyResponse updateChapterOrders(@RequestBody ChapterOrdersRequest chapterOrdersRequest, UserInfo userInfo) {
        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(chapterOrdersRequest.getTopicId(), userInfo.getId());

        chapterService.updateChapterOrders(chapterOrdersRequest.getTopicId(), chapterOrdersRequest.buildChaterListEntity());
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "챕터 삭제")
    @DeleteMapping("/{chapterId}")
    public EmptyResponse deleteChapter(@PathVariable("chapterId") long chapterId, UserInfo userInfo) {
        Chapter chapter = chapterService.getChapter(chapterId);

        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(chapter.getTopic().getId(), userInfo.getId());

        chapterService.deleteChapter(chapter);
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "챕터 목록")
    @GetMapping("")
    public ChapterResponse getChapters(ChapterRequest chapterRequest, UserInfo userInfo) {
        // 토픽의 읽기 권한 체크
        authService.checkUserHasReadRoleAboutTopic(chapterRequest.getTopicId(), userInfo.getId());

        AuthCode role = topicService.selectUserTopicRole(chapterRequest.getTopicId(), userInfo.getId());

        return ChapterResponse.builder()
                .chapters(chapterService.getChapters(chapterRequest.buildChaterEntity()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .topic(new TopicResponse(topicService.selectTopic(chapterRequest.getTopicId())))
                .role(role)
                .build();
    }

    @ApiOperation(value = "챕터 정보")
    @GetMapping("/{chapterId}")
    public ChapterResponse getChapter(@PathVariable("chapterId") long chapterId, UserInfo userInfo) {

        Chapter chapter = chapterService.getChapter(chapterId);

        // 토픽의 읽기 권한 체크
        authService.checkUserHasReadRoleAboutTopic(chapter.getTopic().getId(), userInfo.getId());

        return ChapterResponse.builder()
                .chapter(ChapterModel.builder().build().buildChapterModel(chapterService.getChapter(chapterId)))
                .build();
    }
}
