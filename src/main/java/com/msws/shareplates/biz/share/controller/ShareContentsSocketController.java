package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.entity.ShareUserSocket;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.RoleCode;
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

    private UserInfo getUserInfo (SimpMessageHeaderAccessor headerAccessor) {
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

        // TODO privateY인 경우, 코드가 입력되었는지 확인 (코드 입력 화면이 아직 없음)
        if (!share.getOpenYn()) {
            throw new ServiceException(ServiceExceptionCode.SHARE_NOT_OPENED);
        }

        ShareUser info = ShareUser.builder().user(User.builder().id(userInfo.getId()).build())
                .share(Share.builder().id(shareId).build())
                .status(SocketStatusCode.ONLINE)
                .role(share.getAdminUser().getId().equals(userInfo.getId()) ? RoleCode.ADMIN : RoleCode.MEMBER).build();

        shareService.createOrUpdateShareUser(info);
        ShareUser shareUser = shareService.selectShareUser(shareId, userInfo.getId());

        String sessionId = headerAccessor.getSessionId();

        ShareUserSocket shareUserSocket = shareService.selectShareUserSocket(sessionId);
        if (shareUserSocket != null) {
            shareUserSocket.setShareUser(shareUser);
            shareService.updateShareUserSocket(shareUserSocket);
            shareMessageService.sendUserJoined(shareId, userInfo, info.getRole());
        }

        return shareUser.getId();
    }


}
