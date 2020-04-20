package com.msws.shareplates.common.message.service;

import com.msws.shareplates.common.message.vo.MessageData;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareMessageService {

    @Autowired
    private MessageSendService messageSendService;

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
}
