package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.service.AccessCodeService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.request.ShareRequest;
import com.msws.shareplates.biz.share.vo.request.ShareSearchConditions;
import com.msws.shareplates.biz.share.vo.response.*;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.message.service.ShareMessageService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.aop.annotation.CheckShareAuth;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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

    // 공유를 오픈하는 것은 토픽의 쓰기 권한이 있어야 하고,
    // 공유를 닫는 것은 공유에 권한이 있어야 하고,
    // 공유 정보를 변경하는 것은 토픽의 쓰기 권한이 있어야 한다.
    // 공유 정보를 조회하는 것은 토픽의 읽기 권한이 있어야 한다.

    private ShareResponse getShareResponse(UserInfo userInfo, Share share) {

        ShareUser info = ShareUser.builder().user(User.builder().id(userInfo.getId()).build())
                .share(Share.builder().id(share.getId()).build())
                .status(SocketStatusCode.OFFLINE)
                .role(share.getAdminUser().getId().equals(userInfo.getId()) ? RoleCode.ADMIN : RoleCode.MEMBER)
                .banYn(false).build();

        shareService.createOrUpdateShareUser(info);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "열린 (+ 본인 프라이빗) 공유 리스트 조회")
    @DisableLogin
    @GetMapping("")
    public SharesResponse selectOpenShareList(HttpServletRequest request, UserInfo userInfo, ShareSearchConditions conditions) {
        Long userId = SessionUtil.getUserId(request);
        return new SharesResponse(shareService.selectOpenShareList(userId, conditions));
    }

    @ApiOperation(value = "엑세스 코드로 공유 정보 조회")
    @PostMapping("/code")
    public ShareResponse selectShareByAccessCode(@RequestParam("accessCode") String accessCode, UserInfo userInfo) {
        Share share = shareService.selectShare(accessCode);
        return getShareResponse(userInfo, share);
    }

    @ApiOperation(value = "공유 ID와 엑세스 코드로 공유 정보 조회 ")
    @PostMapping("/{shareId}/register")
    public ShareResponse selectAndRegisterAccessCodeOfPrivateShare(@PathVariable Long shareId, @RequestParam("accessCode") String accessCode, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId, accessCode);
        return getShareResponse(userInfo, share);
    }

    @ApiOperation(value = "토픽 공유 상태 조회 (ID, 열림, 프라이빗)")
    @GetMapping("/{shareId}/status")
    public ShareStatusResponse selectShareStatus(@PathVariable Long shareId) {
        Share share = shareService.selectShare(shareId);
        return new ShareStatusResponse(share);
    }


    @ApiOperation(value = "엑세스 코드 변경")
    @PutMapping("/codes/{accessCodeId}")
    public AccessCodeResponse updateAccessCode(@PathVariable Long accessCodeId, UserInfo userInfo) throws NoSuchProviderException, NoSuchAlgorithmException {
        return new AccessCodeResponse(accessCodeService.updateAccessCode(accessCodeId, userInfo.getId()));
    }

    @ApiOperation(value = "공유 상세 정보 조회")
    @GetMapping("/{shareId}/detail")
    @CheckShareAuth
    public ShareInfo selectShareDetail(@PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);
        return ShareInfo.builder()
                .accessCode(new AccessCodeResponse(accessCodeService.selectAccessCodeByCode(share.getAccessCode())))
                .share(new ShareResponse(share)).build();
    }


    @ApiOperation(value = "공유 정보 수정")
    @PutMapping("/{shareId}")
    @CheckShareAuth
    public ShareResponse updateShare(@PathVariable Long shareId, @RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        Share share = shareService.selectShare(shareRequest.getId());
        share.merge(shareRequest);
        shareService.updateShare(share);
        return new ShareResponse(shareService.selectShareInfo(shareRequest.getId()));
    }

    @ApiOperation(value = "공유 정보 삭제")
    @DeleteMapping("/{shareId}")
    @CheckShareAuth
    public ShareResponse deleteShare(@PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);
        shareService.deleteShare(share);
        return new ShareResponse(share);
    }



    @ApiOperation(value = "공유 열기")
    @PutMapping("/{shareId}/open")
    @CheckShareAuth
    public ShareResponse updateOpenShare(@PathVariable Long shareId, @RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        Share share = shareService.selectShare(shareRequest.getId());
        share.merge(shareRequest);
        shareService.updateShareStart(share, userInfo.getId());
        return new ShareResponse(share);
    }


    @ApiOperation(value = "공유 닫기")
    @PutMapping("/{shareId}/close")
    @CheckShareAuth
    public ShareResponse updateCloseShare(@PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);
        shareService.updateShareStop(share);
        shareMessageService.sendShareClosed(shareId, userInfo);
        return new ShareResponse(share);
    }

}
