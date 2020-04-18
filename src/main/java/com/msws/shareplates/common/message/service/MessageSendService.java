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
import com.msws.shareplates.framework.session.vo.UserInfo;

@Service
public class MessageSendService {
	
	@Autowired
	private MessageBroker messageBroker;
	
	@Autowired
	private ShareService shareService;
	
	@Autowired
	private ShareUserRepository shareUserRepository;
	
	//TODO 토픽 입장 시 사용자별 채널 코드 생성 및 채널 정보 내려주기
	public void sendToShareUser(long shareRoom, long targetUser, ChannelCode targetChannel, Object messageObject, UserInfo userInfo) {
		String uuid = shareUserRepository.findByShareIdAndUserIdAndStatus(shareRoom, targetUser, SocketStatusCode.ONLINE).map(ShareUser::getUuid).orElseThrow(() -> new ServiceException(ServiceExceptionCode.BAD_REQUEST));
		
		String topicUrl = ChannelCode.SHARE_ROOM.getCode() + "/" + shareRoom + "/" + targetChannel.getCode() + "/" + uuid;
		
		messageBroker.pubMessage(topicUrl, messageObject, userInfo);
	}
	
	//TODO 접속 사용자 캐시로 변경할지 말지
	public void sendToShareGroup(String shareRoom, long targetGrouptId, RoleCode targetGroup, ChannelCode targetChannel, Object messageObject, UserInfo userInfo) {
		shareService.selectShare(targetGrouptId).getShareUsers().stream()
			.filter(shareUser -> shareUser.getRole() == targetGroup)
			.forEach(shareUser -> messageBroker.pubMessage(ChannelCode.SHARE_ROOM.getCode() + "/" + shareRoom + "/" + targetChannel.getCode() + "/" + shareUser.getUuid(), messageObject, userInfo));
	}
	
	public void sendToAll(String topicUrl, Object messageObject, UserInfo userInfo) {
		messageBroker.pubMessage(topicUrl, messageObject, userInfo);
	}
}
