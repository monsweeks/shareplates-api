package com.msws.shareplates.common.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.message.MessageBroker;
import com.msws.shareplates.common.message.vo.ChannelCode;
import com.msws.shareplates.framework.session.vo.UserInfo;

@Service
public class MessageSendService {
	
	@Autowired
	private MessageBroker messageBroker;
	
	@Autowired
	private ShareService shareService;
	
	//TODO 토픽 입장 시 사용자별 채널 코드 생성 및 채널 정보 내려주기
	public void sendToUser(String accessCode, String targetUser, ChannelCode targetChannel, Object messageObject, UserInfo userInfo) {
		messageBroker.pubMessage(accessCode, targetUser, targetChannel, messageObject, userInfo);
	}
	
	//TODO 접속 사용자 캐시로 변경할지 말지
	public void sendToGroup(String accessCode, long targetGrouptId, RoleCode targetGroup, ChannelCode targetChannel, Object messageObject, UserInfo userInfo) {
		shareService.selectShare(targetGrouptId).getShareUsers().forEach(shareUser -> messageBroker.pubMessage(accessCode, shareUser.getUuid(), targetChannel, messageObject, userInfo));
	}
}
