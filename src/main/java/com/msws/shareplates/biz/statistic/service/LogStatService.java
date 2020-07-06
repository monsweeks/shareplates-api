package com.msws.shareplates.biz.statistic.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

import lombok.extern.slf4j.Slf4j;

@Profile("default")
@Slf4j
@Service
public class LogStatService implements StatServiceIF<UserAccessCount>{@Override
	
	public Stat_database getName() {
		return Stat_database.log;
	}

	@Override
	public List<UserAccessCount> getData(String key, String value, TimeUnit timeunit, int amount, String timespan) {
		log.info("stat getData : key -> {} , value -> {}, timeunit -> {}, amount -> {}, timespan -> {}", key, value, timeunit, amount, timespan);
		return null;
	}

	@Override
	public List<UserAccessCount> getDetailData(String key, String value, TimeUnit timeunit, int amount) {
		log.info("stat getDetailData : key -> {} , value -> {}, timeunit -> {}, amount -> {}", key, value, timeunit, amount);
		return null;
	}

	@Override
	public void setData(Share data, Long userId, String flag) {
		log.info("stat setData : data -> {} , userId -> {}, flag -> {}", data, userId, flag);
		
	}

	@Override
	public void setData(Long topicId, Long shareId, Long chapterId, Long pageId) {
		log.info("stat setData : topicId -> {} , shareId -> {}, chapterId -> {, pageId -> {}}", topicId, shareId, chapterId, pageId);
		
	}
	


}
