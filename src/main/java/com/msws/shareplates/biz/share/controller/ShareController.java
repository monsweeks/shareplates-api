package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.common.service.AuthService;
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
import com.msws.shareplates.common.message.service.ShareMessageService;
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
    private ShareService shareService;

    @Autowired
    private ShareMessageService shareMessageService;

    @ApiOperation(value = "열린 (+ 본인 프라이빗) 공유 리스트 조회")
    @DisableLogin
    @GetMapping("")
    public SharesResponse selectOpenShareList(HttpServletRequest request) {
        Long userId = SessionUtil.getUserId(request);
        return new SharesResponse(shareService.selectOpenShareList(userId));
    }

    @ApiOperation(value = "토픽 정보 조회 (공유 생성을 위한 기초 정보")
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

    @ApiOperation(value = "공유 정보 조회 (편집)")
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

    @ApiOperation(value = "공유 정보 생성")
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

    @ApiOperation(value = "공유 열기")
    @PutMapping("/{shareId}/open")
    public ShareResponse updateOpenShare(@RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        Share share = shareService.selectShare(shareRequest.getId());

        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutTopic(share.getTopic().getId(), userInfo.getId());

        share.merge(shareRequest);
        shareService.updateShareStart(share, userInfo.getId());
        return new ShareResponse(share);
    }

    @ApiOperation(value = "공유 닫기")
    @PutMapping("/{shareId}/close")
    public ShareResponse updateCloseShare(@PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
        shareService.updateShareStop(share);
        shareMessageService.sendShareClosed(shareId, userInfo);
        return new ShareResponse(share);
    }

}
