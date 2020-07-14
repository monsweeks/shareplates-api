package com.msws.shareplates.biz.statistic.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

public interface StatServiceIF<T> {
	
	public Stat_database getName();
	
	public List<T> getData(String value, Timestamp from, Timestamp to);
	public List<T> getDetailData(String value, Timestamp from, Timestamp to);
	
	public void setData(Share data, Long userId, String flag, Object additional_value);
	
}
