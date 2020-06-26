package com.msws.shareplates.common.message.service;

import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.biz.user.vo.response.ShareUserResponse;
import com.msws.shareplates.common.code.ChatTypeCode;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.ScreenTypeCode;
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
        ShareUserResponse user = new ShareUserResponse(userService.selectUser(userInfo.getId()));
        user.setMessage(shareService.selectLastReadyChat(shareId, userInfo.getId()).getMessage());
        user.setShareRoleCode(role);
        user.setStatus(SocketStatusCode.ONLINE);
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_JOINED).build();
        data.addData("user", user);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendScreenTypeRegistered(long shareId, UserInfo userInfo, ScreenTypeCode screenTypeCode) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.SCREEN_TYPE_REGISTERED).build();
        data.addData("screenType", screenTypeCode);

        // TODO 양일동
        // 1. 어드민에게 메세지 보내기 (동작안함)
        messageSendService.sendToShareGroup(shareId, RoleCode.ADMIN, data, userInfo);
        // 2. 어드민이면서, 특정 ScreenType에만 메세지 보내기 (없는데 추가), ScreenTypeCode는 소켓별로 등록되며, ShareUserSocket에 속성으로 추가되어 있음
        // messageSendService.sendToShareGroup(shareId, RoleCode.ADMIN, screenTypeCode, data, userInfo);
        // 3. 아래 메소드는 언제사용하는거임??
        //messageSendService.sendToShareGroup
    }

    public void sendUserStatusChange(long shareId, UserInfo userInfo, SocketStatusCode statusCode) {
        ShareUser shareUser = shareService.selectShareUser(shareId, userInfo.getId());
        ShareUserResponse user = new ShareUserResponse(shareUser.getUser());
        user.setStatus(statusCode);
        user.setShareRoleCode(shareUser.getRole());
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_STATUS_CHANGE).build();
        data.addData("user", user);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendUserKickOut(long shareId, long userId, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_KICK_OUT).build();
        data.addData("userId", userId);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendUserBan(long shareId, long userId, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_BAN).build();
        data.addData("userId", userId);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendUserAllowed(long shareId, long userId, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_ALLOWED).build();
        data.addData("userId", userId);
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
