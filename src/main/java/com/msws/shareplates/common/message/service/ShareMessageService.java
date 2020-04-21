package com.msws.shareplates.common.message.service;

import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.biz.user.vo.response.UserResponse;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.message.vo.MessageData;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareMessageService {

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private UserService userService;

    public void sendShareStartedChange(long shareId, Boolean startedYn, UserInfo userInfo) {
        String shareUrl = messageSendService.getShareUrl(shareId);
        MessageData data = MessageData.builder().type(MessageData.messageType.SHARE_STARTED_STATUS_CHANGE).build();
        data.addData("startedYn", startedYn);
        // TODO JOIN 처리 후, 공유 그룹에만 전달하도록 변경해야 함
        messageSendService.sendToAll(shareUrl, data, userInfo);
    }

    public void sendCurrentPageChange(long shareId, long chapterId, long pageId, UserInfo userInfo) {
        String shareUrl = messageSendService.getShareUrl(shareId);
        MessageData data = MessageData.builder().type(MessageData.messageType.CURRENT_PAGE_CHANGE).build();
        data.addData("chapterId", chapterId);
        data.addData("pageId", pageId);
        // TODO JOIN 처리 후, 공유 그룹에만 전달하도록 변경해야 함
        messageSendService.sendToAll(shareUrl, data, userInfo);
    }

    public void sendUserJoined(long shareId, UserInfo userInfo, RoleCode role) {
        String shareUrl = messageSendService.getShareUrl(shareId);
        UserResponse user = new UserResponse(userService.selectUser(userInfo.getId()));
        user.setShareRoleCode(role);
        MessageData data = MessageData.builder().type(MessageData.messageType.USER_JOINED).build();
        data.addData("user", user);
        // TODO JOIN 처리 후, 공유 그룹에만 전달하도록 변경해야 함
        messageSendService.sendToAll(shareUrl, data, userInfo);
    }

    public void sendReadyChat(long shareId, String lastMessage, UserInfo userInfo) {
        String shareUrl = messageSendService.getShareUrl(shareId);
        MessageData data = MessageData.builder().type(MessageData.messageType.READY_CHAT).build();
        data.addData("message", lastMessage);
        data.addData("senderId", userInfo.getId());
        // TODO JOIN 처리 후, 공유 그룹에만 전달하도록 변경해야 함
        messageSendService.sendToAll(shareUrl, data, userInfo);
    }
}
