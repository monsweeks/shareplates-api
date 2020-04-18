package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.page.service.PageService;
import com.msws.shareplates.biz.page.vo.PageModel;
import com.msws.shareplates.biz.page.vo.response.PageResponse;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.AccessCodeService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.request.ShareRequest;
import com.msws.shareplates.biz.share.vo.response.AccessCodeResponse;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.share.vo.response.ShareResponse;
import com.msws.shareplates.biz.share.vo.response.SharesResponse;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/shares")
public class ShareController {
    @Autowired
    AuthService authService;
    @Autowired
    private AccessCodeService accessCodeService;
    @Autowired
    private TopicService topicService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ShareService shareService;

    @DisableLogin
    @GetMapping("")
    public SharesResponse selectShareList(HttpServletRequest request) {

        Long userId = SessionUtil.getUserId(request);

        return new SharesResponse(shareService.selectOpenShareList(userId));
    }

    @ApiOperation(value = "공유 정보 생성을 위한 토픽 정보 조회")
    @GetMapping("/topics/{topicId}")
    public ShareInfo selectTopicShareInfo(@PathVariable Long topicId, UserInfo userInfo) throws NoSuchProviderException, NoSuchAlgorithmException {
        // 토픽의 읽기 권한 체크
        authService.checkUserHasReadRoleAboutTopic(topicId, userInfo.getId());

        return ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(topicId)))
                .chapters(chapterService.selectChapters(topicId).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .accessCode(new AccessCodeResponse(accessCodeService.createAccessCode(userInfo.getId())))
                .build();
    }

    @ApiOperation(value = "공유를 생성하기 위한 정보 조회")
    @GetMapping("/{shareId}/info")
    public ShareInfo selectShare(@PathVariable Long shareId, UserInfo userInfo) {

        Share share = shareService.selectShare(shareId);
        // 토픽의 읽기 권한 체크
        authService.checkUserHasReadRoleAboutTopic(share.getTopic().getId(), userInfo.getId());

        return ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(share.getTopic().getId())))
                .chapters(chapterService.selectChapters(share.getTopic().getId()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .accessCode(new AccessCodeResponse(accessCodeService.selectAccessCodeByCode(share.getAccessCode())))
                .share(new ShareResponse(share))
                .build();
    }

    @ApiOperation(value = "새로운 공유 시작")
    @PostMapping("")
    public ShareResponse createShare(@RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutTopic(shareRequest.getTopicId(), userInfo.getId());
        Share share = shareService.createShare(new Share(shareRequest), userInfo.getId());
        return new ShareResponse(share);
    }

    @ApiOperation(value = "공유 정보 수정")
    @PutMapping("/{shareId}")
    public ShareResponse updateShare(@PathVariable Long shareId, @RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        Share share = shareService.selectShare(shareRequest.getId());

        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutTopic(share.getTopic().getId(), userInfo.getId());

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.merge(shareRequest);
        shareService.updateShare(share);
        return new ShareResponse(shareService.selectShareInfo(shareRequest.getId()));
    }

    @ApiOperation(value = "기존 공유 다시 시작")
    @PutMapping("/{shareId}/start")
    public ShareResponse updateStartShare(@RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        Share share = shareService.selectShare(shareRequest.getId());

        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutTopic(share.getTopic().getId(), userInfo.getId());

        share.merge(shareRequest);
        shareService.updateShareStart(share, userInfo.getId());
        return new ShareResponse(share);
    }

    @ApiOperation(value = "공유 중지")
    @PutMapping("/{shareId}/stop")
    public ShareResponse updateStopShare(@PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutTopic(share.getTopic().getId(), userInfo.getId());
        shareService.updateShareStop(share);
        return new ShareResponse(share);
    }

    @ApiOperation(value = "공유 정보 삭제")
    @DeleteMapping("/{shareId}")
    public ShareResponse deleteShare(@PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);
        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutTopic(share.getTopic().getId(), userInfo.getId());
        shareService.deleteShare(share);
        return new ShareResponse(share);
    }

    @ApiOperation(value = "엑세스 코드 변경")
    @PutMapping("/codes/{accessCodeId}")
    public AccessCodeResponse updateAccessCode(@PathVariable Long accessCodeId, UserInfo userInfo) throws NoSuchProviderException, NoSuchAlgorithmException {
        return new AccessCodeResponse(accessCodeService.updateAccessCode(accessCodeId, userInfo.getId()));
    }
    
    //TODO 권한 체크 후 쉐어사용자 정보 생성 및 갱신
    @ApiOperation(value = "공유방 참가")
    @PutMapping("/{shareId}/join")
    public ShareResponse joinShare(@PathVariable Long shareId, UserInfo userInfo) {

        Share share = shareService.selectShare(shareId);
        // 토픽의 읽기 권한 체크
        authService.checkUserHasReadRoleAboutTopic(share.getTopic().getId(), userInfo.getId());
    	
    	return ShareResponse.builder().uuid(userInfo.getUuid()).build();
    }

    @ApiOperation(value = "공유 컨텐츠 조회")
    @GetMapping("/{shareId}")
    public ShareInfo selectShareContent(@PathVariable Long shareId, UserInfo userInfo) {

        Share share = shareService.selectShare(shareId);
        // TODO private인 경우, 코드가 입력되었는지 확인해야 한다.

        return ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(share.getTopic().getId())))
                .chapters(chapterService.selectChapters(share.getTopic().getId()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .share(new ShareResponse(share))
                .build();
    }

    @ApiOperation(value = "공유 챕터의 페이지 목록 조회")
    @GetMapping("/{shareId}/chapters/{chapterId}/pages")
    public PageResponse selectSharePageList(@PathVariable Long shareId, @PathVariable Long chapterId, UserInfo userInfo) {

        // TODO private인 경우, 코드가 입력되었는지 확인해야 한다.
        Share share = shareService.selectShare(shareId);

        return PageResponse.builder()
                .pages(pageService.selectPages(share.getTopic().getId(), chapterId).stream()
                        .map(page -> PageModel.builder().build().buildPageModel(page))
                        .collect(Collectors.toList()))
                .build();
    }

}
