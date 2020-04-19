package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.service.PageService;
import com.msws.shareplates.biz.page.vo.PageModel;
import com.msws.shareplates.biz.page.vo.response.PageResponse;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.share.vo.response.ShareResponse;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.message.service.ShareMessageService;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/shares/{share-id}/contents")
public class ShareContentsController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShareMessageService shareMessageService;

    @ApiOperation(value = "공유 정보 조회 (공유)")
    @GetMapping("")
    public ShareInfo selectShareContent(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {

        Share share = shareService.selectShare(shareId);
        // TODO private인 경우, 코드가 입력되었는지 확인해야 한다.

        return ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(share.getTopic().getId())))
                .chapters(chapterService.selectChapters(share.getTopic().getId()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .share(new ShareResponse(share))
                .build();
    }


    @ApiOperation(value = "공유 챕터 페이지 목록 조회")
    @GetMapping("/chapters/{chapterId}/pages")
    public PageResponse selectSharePageList(@PathVariable(value = "share-id") long shareId, @PathVariable Long chapterId, UserInfo userInfo) {

        // TODO private인 경우, 코드가 입력되었는지 확인해야 한다.
        Share share = shareService.selectShare(shareId);

        return PageResponse.builder()
                .pages(pageService.selectPages(share.getTopic().getId(), chapterId).stream()
                        .map(page -> PageModel.builder().build().buildPageModel(page))
                        .collect(Collectors.toList()))
                .build();
    }


    @ApiOperation(value = "공유 시작")
    @PutMapping("/start")
    public ShareResponse updateStartShare(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.setStartedYn(true);
        shareService.updateShare(share);

        shareMessageService.sendShareStartedChange(shareId, true, userInfo);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "공유 중지")
    @PutMapping("/stop")
    public ShareResponse updateStopShare(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.setStartedYn(false);
        shareService.updateShare(share);

        shareMessageService.sendShareStartedChange(shareId, false, userInfo);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "페이지 이동")
    @PutMapping("/current/chapters/{chapterId}/pages/{pageId}")
    public ShareResponse updateCurrentPosition(@PathVariable(value = "share-id") long shareId, @PathVariable long chapterId, @PathVariable long pageId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.setCurrentChapter(Chapter.builder().id(chapterId).build());
        share.setCurrentPage(Page.builder().id(pageId).build());
        shareService.updateShare(share);

        shareMessageService.sendCurrentPageChange(shareId, chapterId, pageId, userInfo);

        return new ShareResponse(share);
    }


    //TODO 권한 체크 후 쉐어사용자 정보 생성 및 갱신
    @ApiOperation(value = "공유방 참가")
    @PutMapping("/join")
    public ShareResponse joinShare(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {
        // TODO 가벼운 쿼리로 변경
        Share share = shareService.selectShare(shareId);
        // TODO privateY인 경우, 코드가 입력되었는지 확인 (코드 입력 화면이 아직 없음)
        if (!share.getOpenYn()) {
            throw new ServiceException(ServiceExceptionCode.SHARE_NOT_OPENED);
        }
        return ShareResponse.builder().uuid(userInfo.getUuid()).build();
    }


}
