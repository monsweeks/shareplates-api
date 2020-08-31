package com.msws.shareplates.common.message.service;

import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.ScrollInfo;
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
        messageSendService.sendToShareGroup(shareId, RoleCode.ADMIN, data, userInfo);
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

    public void sendScrollInfoChange(long shareId, ScrollInfo scrollInfo, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.SCROLL_INFO_CHANGED).build();
        data.addData("scrollInfo", scrollInfo);
        messageSendService.sendToShareGroup(shareId, RoleCode.ADMIN, data, userInfo);
    }

    public void sendMoveScroll(long shareId, String dir , UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.MOVE_SCROLL).build();
        data.addData("dir", dir);
        messageSendService.sendToShare(shareId, data, userInfo);
    }

    public void sendUserFocusChange(long shareId, long userid, boolean focus, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.USER_FOCUS_CHANGE).build();
        data.addData("userId", userid);
        data.addData("focus", focus);
        messageSendService.sendToShareGroup(shareId, RoleCode.ADMIN, data, userInfo);
    }

    public void sendOptionChange(long shareId, String optionsKey, Object optionValue, UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.OPTION_CHANGE).build();
        data.addData("optionKey", optionsKey);
        data.addData("optionValue", optionValue);
        messageSendService.sendToShareGroup(shareId, RoleCode.ADMIN, data, userInfo);
    }

    public void sendPointerChange(long shareId, Long itemId, Long index1 , Long index2, String style, String color,  UserInfo userInfo) {
        MessageData data = MessageData.builder().type(MessageData.MessageType.POINTER_CHANGE).build();
        data.addData("itemId", itemId);
        data.addData("index1", index1);
        data.addData("index2", index2);
        data.addData("style", style);
        data.addData("color", color);
        messageSendService.sendToShare(shareId, data, userInfo);
    }


}
