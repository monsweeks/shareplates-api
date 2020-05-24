package com.msws.shareplates.common.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.msws.shareplates.common.message.vo.MessageInfo;
import com.msws.shareplates.common.message.vo.MessageInfo.SenderInfo;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.java.Log;

@Log
@Service
public class MessageSendService {

	@Autowired
	private MessageBroker messageBroker;

	@Autowired
	private ShareService shareService;

	@Autowired
	private ShareUserRepository shareUserRepository;

	// TODO 토픽 입장 시 사용자별 채널 코드 생성 및 채널 정보 내려주기
	public void sendToShareUser(String sessionId, long shareId, long targetUser, ChannelCode targetChannel,
			MessageData messageData, UserInfo userInfo) {
		ShareUser shareUser = shareUserRepository
				.findByShareIdAndUserIdAndStatus(shareId, targetUser, SocketStatusCode.ONLINE)
				.orElseThrow(() -> new ServiceException(ServiceExceptionCode.BAD_REQUEST));
		
		String topicUrl = ChannelCode.SHARE_ROOM.getCode() + "/" + shareId + "/" + targetChannel.getCode();

		messageBroker.pubMessage(MessageInfo.builder()
									.userId(String.valueOf(shareUser.getId()))
									.topicUrl(topicUrl)
									.data(messageData)
									.senderInfo(SenderInfo.builder().id(userInfo.getId())
									.build()).build());
	}

	// TODO 접속 사용자 캐시로 변경할지 말지
	public void sendToShareGroup(long shareId, RoleCode targetGroup, MessageData messageData, UserInfo userInfo) {
		shareService.selectShare(shareId).getShareUsers().stream()
				.filter(shareUser -> shareUser.getRole().equals(targetGroup))
				.forEach(shareUser -> messageBroker.pubMessage(MessageInfo.builder()
						.userId(String.valueOf(shareUser.getUser().getId()))
						.topicUrl(ChannelCode.SHARE_ROOM.getCode() + "/" + shareId)
						.data(messageData)
						.senderInfo(SenderInfo.builder().id(userInfo.getId())
						.build()).build()));
	}

	// TODO 접속 사용자 캐시로 변경할지 말지
	public void sendToShare(long shareId, MessageData messageData, UserInfo userInfo) {
		messageBroker.pubMessage(MessageInfo.builder()
				.topicUrl(ChannelCode.SHARE_ROOM.getCode() + "/" + shareId)
				.data(messageData)
				.senderInfo(SenderInfo.builder().id(userInfo.getId()).build())
				.build());
	}

	public void sendToAll(String topicUrl, MessageData messageData, UserInfo userInfo) {
		messageBroker.pubMessage(MessageInfo.builder()
						.topicUrl(topicUrl)
						.data(messageData)
						.senderInfo(SenderInfo.builder().id(userInfo.getId())
						.build()).build());
	}

	public String getShareUrl(long shareId) {
		return ChannelCode.SHARE_ROOM.getCode() + "/" + shareId;
	}
}
