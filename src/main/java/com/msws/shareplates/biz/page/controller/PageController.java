package com.msws.shareplates.biz.page.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.code.RoleCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.file.entity.FileInfo;
import com.msws.shareplates.biz.file.service.FileInfoService;
import com.msws.shareplates.biz.file.vo.FileInfoResponse;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.service.PageService;
import com.msws.shareplates.biz.page.vo.PageModel;
import com.msws.shareplates.biz.page.vo.request.PageOrdersRequest;
import com.msws.shareplates.biz.page.vo.request.PageRequest;
import com.msws.shareplates.biz.page.vo.response.PageResponse;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.TopicModel;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.aop.annotation.CheckTopicAuth;
import com.msws.shareplates.framework.session.vo.UserInfo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/topics/{topic-id}/chapters/{chapter-id}/pages")
@CheckTopicAuth
public class PageController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private PageService pageService;

    @Autowired
    private FileInfoService fileStorageService;

    @Autowired
    private FileInfoService fileInfoService;

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
        Page page = pageService.selectPage(topicId, chapterId, pageId);
        page.setContent(pageRequest.getContent());
        pageService.updatePage(page);
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
        AuthCode role = topicService.selectUserTopicRole(topicId, userInfo.getId());
        if (RoleCode.SUPER_MAN == userInfo.getRoleCode()) role = AuthCode.WRITE;

    	Chapter chapter = chapterService.selectChapter(chapterId, topicId);
    	
        return PageResponse.builder()
                .topic(TopicModel.builder().build().buildTopicModel(chapter.getTopic()))
                .pages(chapter.getPages().stream()
                        .map(page -> PageModel.builder().build().buildPageModel(page))
                        .collect(Collectors.toList()))
                .chapter(ChapterModel.builder().build().buildChapterModel(chapter))
                .role(role)
                .build();
    }

    @ApiOperation(value = "페이지 정보")
    @GetMapping("/{page-id}")
    public PageResponse selectPage(@PathVariable(value = "topic-id") long topicId, @PathVariable(value = "chapter-id") long chapterId, @PathVariable("page-id") long pageId, UserInfo userInfo) {

        return PageResponse.builder()
                .page(PageModel.builder().build().buildPageModel(pageService.selectPage(topicId, chapterId, topicId)))
                .build();
    }

    @PostMapping("/{page-id}/file")
    public FileInfoResponse uploadFile(@PathVariable(value = "topic-id") long topicId, @PathVariable(value = "chapter-id") long chapterId, @PathVariable("page-id") long pageId, @RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("size") Long size, @RequestParam("type") String type, HttpServletRequest req) {

        String fileName = fileStorageService.storeFile(file, req);

        FileInfo fileInfo = FileInfo.builder().topicId(topicId)
                .chapterId(chapterId)
                .pageId(pageId)
                .path(fileName)
                .name(name)
                .size(size)
                .type(type)
                .uuid(UUID.randomUUID().toString().replaceAll("-", ""))
                .build();

        fileInfoService.createFileInfo(fileInfo);

        return new FileInfoResponse(fileInfo.getId(), fileInfo.getUuid());
    }


}
