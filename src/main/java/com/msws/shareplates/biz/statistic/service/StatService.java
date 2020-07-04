package com.msws.shareplates.biz.statistic.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.common.exception.StatDBException;


@Service
public class StatService {
	
	private final StatServiceIF<?> mainService;
	
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
	public void writeJoinData(Share selectShare, Long userId) {
		mainService.setData(selectShare, userId, "join");
	}
	
	@Async
	public void writeDisconnectData(Share selectShare, Long userId) {
		mainService.setData(selectShare, userId, "out");
	}

	@Async
	public void writePageChanged(Long topicId, Long shareId, Long chapterId, Long pageId) {
		// 챕터, 페이지 이동됨
	}

	@Async
	public void writeJoinUser(Long topicId, Long shareId, Long userId) {
		// 새로운 사용자가 참여함
		// 새로운 사용자의 세션이 1개 추가됨
	}

	@Async
	public void writeDisconnectUser(Long topicId, Long shareId, Long userId) {
		// 사용자가 나감
		// 사용자의 세션이 1개 감소됨
	}

	@Async
	public void writeAddUserSession(Long topicId, Long shareId, Long userId) {
		// 사용자의 세션 추가됨
	}

	@Async
	public void writeDisconnectUserSession(Long topicId, Long shareId, Long userId) {
		// 사용자의 세션이 나감
	}

	@Async
	public void writeUserFocusChange(Long topicId, Long shareId, Long userId, Boolean focus) {
		// 사용자의 포커스 여부가 변경됨
	}

	
	public List<UserAccessCount> getData(String key, String value, TimeUnit timeunit, int amount, String timespan) {
		return (List<UserAccessCount>) mainService.getData(key, value, timeunit, amount,timespan);
	}
	
	public List<UserAccessCount> getDetailData(String key, String value, TimeUnit timeunit, int amount) {
		return (List<UserAccessCount>) mainService.getDetailData(key, value, timeunit, amount);
	}
	
}
