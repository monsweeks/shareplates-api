package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.entity.ShareUserSocket;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.ScreenTypeCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.message.service.ShareMessageService;
import com.msws.shareplates.framework.session.vo.UserInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log
@RestController
@MessageMapping("/api/shares/{shareId}/contents")
public class ShareContentsSocketController {

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShareMessageService shareMessageService;

    @Autowired
    private StatService statService;

    private UserInfo getUserInfo(SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> attributes = headerAccessor.getSessionAttributes();

        if (attributes == null) {
            throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER);
        }

        UserInfo userInfo = (UserInfo) attributes.get("USER_INFO");

        if (userInfo == null) {
            throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER);
        }

        return userInfo;
    }

    @MessageMapping("/join")
    public Long join(@DestinationVariable(value = "shareId") long shareId, SimpMessageHeaderAccessor headerAccessor) {

        Share share = shareService.selectShareInfo(shareId);
        UserInfo userInfo = this.getUserInfo(headerAccessor);

        if (!share.getOpenYn()) {
            throw new ServiceException(ServiceExceptionCode.SHARE_NOT_OPENED);
        }

        if (shareService.selectIsBanUser(shareId, userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.SHARE_BANNED_USER);
        }

        ShareUser info = ShareUser.builder().user(User.builder().id(userInfo.getId()).build())
                .share(Share.builder().id(shareId).build())
                .status(SocketStatusCode.ONLINE)
                .focusYn(true)
                .role(share.getAdminUser().getId().equals(userInfo.getId()) ? RoleCode.ADMIN : RoleCode.MEMBER)
                .banYn(false).build();

        boolean isNewUser = shareService.createOrUpdateShareUser(info);
        ShareUser shareUser = shareService.selectShareUser(shareId, userInfo.getId());

        String sessionId = headerAccessor.getSessionId();

        ShareUserSocket shareUserSocket = shareService.selectShareUserSocket(sessionId);
        if (shareUserSocket != null) {
            shareUserSocket.setShareUser(shareUser);
            shareUserSocket.setFocusYn(true);
            shareService.updateShareUserSocket(shareUserSocket);
            shareMessageService.sendUserJoined(shareId, userInfo, info.getRole());
            shareMessageService.sendUserFocusChange(shareId, userInfo.getId(), true, userInfo);

            // 통계 정보, 사용자 (새로운 사용자 소켓 추가)
            if (isNewUser) {
                statService.writeJoinUser(share.getTopic().getId(), share.getId(), userInfo.getId());
            } else {
                statService.writeAddUserSession(share.getTopic().getId(), share.getId(), userInfo.getId());
            }

            statService.writeUserFocusChange(share.getTopic().getId(), shareId, userInfo.getId(), true);
        }

        return shareUser.getId();
    }

    @MessageMapping("/screenType")
    public void screenType(@DestinationVariable(value = "shareId") long shareId, String screenType, SimpMessageHeaderAccessor headerAccessor) {

        Share share = shareService.selectShareInfo(shareId);
        UserInfo userInfo = this.getUserInfo(headerAccessor);

        if (!share.getOpenYn()) {
            throw new ServiceException(ServiceExceptionCode.SHARE_NOT_OPENED);
        }

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        String sessionId = headerAccessor.getSessionId();
        ShareUserSocket shareUserSocket = shareService.selectShareUserSocket(sessionId);
        shareUserSocket.setScreenTypeCode(ScreenTypeCode.valueOf(screenType));
        if (shareUserSocket != null) {
            shareService.updateShareUserSocket(shareUserSocket);
            shareMessageService.sendScreenTypeRegistered(shareId, userInfo, ScreenTypeCode.valueOf(screenType));
        }
    }

    @MessageMapping("/focus")
    public void focus(@DestinationVariable(value = "shareId") long shareId, Boolean focus, SimpMessageHeaderAccessor headerAccessor) {

        Share share = shareService.selectShareInfo(shareId);
        UserInfo userInfo = this.getUserInfo(headerAccessor);

        if (!share.getOpenYn()) {
            throw new ServiceException(ServiceExceptionCode.SHARE_NOT_OPENED);
        }

        String sessionId = headerAccessor.getSessionId();
        ShareUserSocket shareUserSocket = shareService.selectShareUserSocket(sessionId);
        shareUserSocket.setFocusYn(focus);
        shareService.updateShareUserSocket(shareUserSocket);

        ShareUser shareUser = shareService.selectShareUser(shareId, userInfo.getId());
        Long focusedCount = shareService.selectFocusedSocketCount(shareId, userInfo.getId());

        if (shareUser.getFocusYn() != (focusedCount > 0)) {
            shareUser.setFocusYn(focusedCount > 0);
            shareService.updateShareUser(shareUser);
            shareMessageService.sendUserFocusChange(shareId, userInfo.getId(), focusedCount > 0, userInfo);

            // 통계 정보, 포커스 여부 변경
            statService.writeUserFocusChange(share.getTopic().getId(), shareId, userInfo.getId(), focusedCount > 0);
        }
    }
}



































