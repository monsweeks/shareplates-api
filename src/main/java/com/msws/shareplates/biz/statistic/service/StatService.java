package com.msws.shareplates.biz.statistic.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.biz.statistic.vo.response.PageChangedInfo;
import com.msws.shareplates.biz.statistic.vo.response.ShareAccessInfo;
import com.msws.shareplates.common.exception.StatDBException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatService {
	
	private final StatServiceIF<?> mainService;
	
	@Autowired
    private ShareService shareService;
	
	@Autowired
	public StatService(List<StatServiceIF<?>> services, 
			@Value("${stat.database}") Stat_database database) {
		
		this.mainService = services.stream().filter( e -> e.getName() == database)
					.findFirst()
					.orElseThrow( () ->
							new StatDBException("no stat database selected")
		);
		
	}

	@Async
	public void writePageChanged(Long topicId, Long shareId, Long chapterId, Long pageId) {
		try {
			Share tempShare = shareService.selectShare(shareId);
			mainService.setData( tempShare,  tempShare.getAdminUser().getId() , "page_changed", null);
		}catch(Exception e) {
			log.error("failed to write PageChanged info : {}", e.getMessage());
		}
	}
	
	@Async
	public void writeUserFocusChange(Long topicId, Long shareId, Long userId, Boolean focus) {
		// 사용자의 포커스 여부가 변경됨
		try {
		mainService.setData(shareService.selectShare(shareId),  userId , "focus_changed", focus);
		}catch(Exception e) {
			log.error("failed to write UserFocusChange info : {}", e.getMessage());
		}
	}

	@Async
	public void writeJoinUser(Long topicId, Long shareId, Long userId) {
		// 새로운 사용자가 참여함
		// 새로운 사용자의 세션이 1개 추가됨
		try {
		mainService.setData(shareService.selectShare(shareId),  userId , "join", null);
		}catch(Exception e) {
			log.error("failed to write JoinUser info : {}", e.getMessage());
		}
	}

	@Async
	public void writeDisconnectUser(Long topicId, Long shareId, Long userId) {
		// 사용자가 나감
		// 사용자의 세션이 1개 감소됨
		try {
		mainService.setData(shareService.selectShare(shareId),  userId , "out", null);
		}catch(Exception e) {
			log.error("failed to write DisconnectUser info : {}", e.getMessage());
		}
	}

	@Async
	public void writeAddUserSession(Long topicId, Long shareId, Long userId) {
		// 사용자의 세션 추가됨
		try {
		mainService.setData(shareService.selectShare(shareId),  userId , "join", null);
		}catch(Exception e) {
			log.error("failed to write AddUserSession info : {}", e.getMessage());
		}
	}

	@Async
	public void writeDisconnectUserSession(Long topicId, Long shareId, Long userId) {
		// 사용자의 세션이 나감
		try {
			mainService.setData(shareService.selectShare(shareId),  userId , "out", null);
		}catch(Exception e) {
			log.error("failed to write DisconnectUserSession info: {}", e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ShareAccessInfo> getDataBetweenSpecificTime(Long shareId, Timestamp from, Timestamp to) {
		return (List<ShareAccessInfo>) mainService.getData(shareId.toString(), from, to);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageChangedInfo> getDetailDataBetweenSpecificTime(Long shareId, Timestamp from, Timestamp to) {
		return (List<PageChangedInfo>) mainService.getDetailData(shareId.toString(), from, to);
	}
}
