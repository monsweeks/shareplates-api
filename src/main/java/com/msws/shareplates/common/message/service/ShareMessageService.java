package com.msws.shareplates.common.message.service;

import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.biz.user.vo.response.UserResponse;
import com.msws.shareplates.common.code.ChatTypeCode;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;
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

    @Autowired
    private ShareService shareService;

    public void sendShareClosed(long shareId, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.SHARE_CLOSED).build();
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendShareStartedChange(long shareId, Boolean startedYn, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.SHARE_STARTED_STATUS_CHANGE).build();
        data.addData("startedYn", startedYn);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendCurrentPageChange(long shareId, long chapterId, long pageId, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.CURRENT_PAGE_CHANGE).build();
        data.addData("chapterId", chapterId);
        data.addData("pageId", pageId);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendUserJoined(long shareId, UserInfo userInfo, RoleCode role) {
        UserResponse user = new UserResponse(userService.selectUser(userInfo.getId()));
        user.setShareRoleCode(role);
        user.setStatus(SocketStatusCode.ONLINE);
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_JOINED).build();
        data.addData("user", user);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendUserStatusChange(long shareId, UserInfo userInfo, SocketStatusCode statusCode) {
        ShareUser shareUser = shareService.selectShareUser(shareId, userInfo.getId());
        UserResponse user = new UserResponse(shareUser.getUser());
        user.setStatus(statusCode);
        user.setShareRoleCode(shareUser.getRole());
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_STATUS_CHANGE).build();
        data.addData("user", user);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendChat(long shareId, ChatTypeCode type, String message, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.CHAT_MESSAGE).build();
        data.addData("type", type);
        data.addData("message", message);
        data.addData("senderId", userInfo.getId());
        messageSendService.sendToShare(shareId, data, userInfo);
    }
}
