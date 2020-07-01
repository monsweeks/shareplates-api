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
	
	public List<UserAccessCount> getData(String key, String value, TimeUnit timeunit, int amount, String timespan) {
		return (List<UserAccessCount>) mainService.getData(key, value, timeunit, amount,timespan);
	}
	
	public List<UserAccessCount> getDetailData(String key, String value, TimeUnit timeunit, int amount) {
		return (List<UserAccessCount>) mainService.getDetailData(key, value, timeunit, amount);
	}
	
}
