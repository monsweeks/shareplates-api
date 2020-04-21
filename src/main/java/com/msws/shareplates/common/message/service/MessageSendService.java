package com.msws.shareplates.common.message.service;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.repository.ShareUserRepository;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.message.MessageBroker;
import com.msws.shareplates.common.message.vo.ChannelCode;
import com.msws.shareplates.common.message.vo.MessageData;
import com.msws.shareplates.framework.session.vo.UserInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Service
public class MessageSendService {

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShareUserRepository shareUserRepository;

    //TODO 토픽 입장 시 사용자별 채널 코드 생성 및 채널 정보 내려주기
    public void sendToShareUser(long shareId, long targetUser, ChannelCode targetChannel, MessageData messageData, UserInfo userInfo) {
        String uuid = shareUserRepository.findByShareIdAndUserIdAndStatus(shareId, targetUser, SocketStatusCode.ONLINE).map(ShareUser::getUuid).orElseThrow(() -> new ServiceException(ServiceExceptionCode.BAD_REQUEST));

        String topicUrl = ChannelCode.SHARE_ROOM.getCode() + "/" + shareId + "/" + targetChannel.getCode() + "/" + uuid;

        messageBroker.pubMessage(topicUrl, messageData, userInfo);
    }

    //TODO 접속 사용자 캐시로 변경할지 말지
    public void sendToShareGroup(long shareId, RoleCode targetGroup, MessageData messageData, UserInfo userInfo) {
        shareService.selectShare(shareId).getShareUsers().stream()
                .filter(shareUser -> shareUser.getRole() == targetGroup)
                .forEach(shareUser -> messageBroker.pubMessage(ChannelCode.SHARE_ROOM.getCode() + "/" + shareId + "/" + shareUser.getUuid(), messageData, userInfo));
    }

    //TODO 접속 사용자 캐시로 변경할지 말지
    public void sendToShare(long shareId, MessageData messageData, UserInfo userInfo) {
        List<ShareUser> shareUsers = shareService.selectShareUserList(shareId);
        shareUsers.stream().filter(shareUser -> shareUser.getStatus() == SocketStatusCode.ONLINE)
                .forEach(shareUser -> messageBroker.pubMessage(ChannelCode.SHARE_ROOM.getCode() + "/" + shareId + "/" + shareUser.getUuid(), messageData, userInfo));
    }

    public void sendToAll(String topicUrl, MessageData messageData, UserInfo userInfo) {
        messageBroker.pubMessage(topicUrl, messageData, userInfo);
    }

    public String getShareUrl(long shareId) {
        return ChannelCode.SHARE_ROOM.getCode() + "/" + shareId;
    }
}
