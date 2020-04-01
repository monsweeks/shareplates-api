package com.msws.shareplates.biz.page.controller;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.service.PageService;
import com.msws.shareplates.biz.page.vo.PageModel;
import com.msws.shareplates.biz.page.vo.request.PageOrdersRequest;
import com.msws.shareplates.biz.page.vo.request.PageRequest;
import com.msws.shareplates.biz.page.vo.response.PageResponse;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics/{topic-id}/chapters/{chapter-id}/pages")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "페이지 생성")
    @PostMapping("")
    public PageResponse createPage(@PathVariable(value = "topic-id") long topicId, @PathVariable(value = "chapter-id") long chapterId, @RequestBody PageRequest pageRequest, UserInfo userInfo) {
        Page page = pageService.createPage(topicId, chapterId, pageRequest.buildPageEntity());
        return PageResponse.builder()
                .page(PageModel.builder().build().buildPageModel(page))
                .build();
    }

    @ApiOperation(value = "페이지 수정")
    @PutMapping("/{page-id}")
    public PageResponse updatePage(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapter-id") long chapterId, @PathVariable("page-id") long pageId, @RequestBody PageRequest pageRequest, UserInfo userInfo) {
        Page page = pageService.updatePage(pageRequest.buildPageEntity());
        return PageResponse.builder()
                .page(PageModel.builder().build().buildPageModel(page))
                .build();
    }

    @ApiOperation(value = "페이지 제목 수정")
    @PutMapping("/{page-id}/title")
    public PageResponse updatePageTitle(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapter-id") long chapterId, @PathVariable("page-id") long pageId, @RequestBody PageRequest pageRequest, UserInfo userInfo) {

        Page page = pageService.selectPage(topicId, chapterId, pageId);
        pageService.updatePage(page.setTitle(pageRequest.getTitle()));

        return PageResponse.builder()
                .page(PageModel.builder().build().buildPageModel(page))
                .build();
    }

    @ApiOperation(value = "페이지 순서 변경")
    @PutMapping("/orders")
    public EmptyResponse updatePageOrders(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapter-id") long chapterId, @RequestBody PageOrdersRequest pageOrdersRequest, UserInfo userInfo) {

        pageService.updatePageOrders(topicId, chapterId, pageOrdersRequest.buildPageListEntity());
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "페이지 삭제")
    @DeleteMapping("/{page-id}")
    public EmptyResponse deletePage(@PathVariable(value = "topic-id") long topicId, @PathVariable("chapter-id") long chapterId, @PathVariable("page-id") long pageId, UserInfo userInfo) {
        pageService.deletePage(topicId, chapterId, pageId);
        return EmptyResponse.getInstance();
    }

    @ApiOperation(value = "페이지 목록")
    @GetMapping("")
    public PageResponse selectPages(@PathVariable(value = "topic-id") long topicId, @PathVariable(value = "chapter-id") long chapterId, UserInfo userInfo) {

        return PageResponse.builder()
                .pages(pageService.selectPages(topicId, chapterId).stream()
                        .map(page -> PageModel.builder().build().buildPageModel(page))
                        .collect(Collectors.toList()))
                .chapter(ChapterModel.builder().build().buildChapterModel(chapterService.selectChapter(chapterId, topicId)))
                .build();
    }

    @ApiOperation(value = "페이지 정보")
    @GetMapping("/{page-id}")
    public PageResponse selectPage(@PathVariable(value = "topic-id") long topicId, @PathVariable(value = "chapter-id") long chapterId, @PathVariable("page-id") long pageId, UserInfo userInfo) {

        return PageResponse.builder()
                .page(PageModel.builder().build().buildPageModel(pageService.selectPage(topicId, chapterId, topicId)))
                .build();
    }


}
