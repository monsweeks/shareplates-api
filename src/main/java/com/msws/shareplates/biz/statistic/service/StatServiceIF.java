package com.msws.shareplates.biz.statistic.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

public interface StatServiceIF<T> {
	
	public Stat_database getName();
	public List<T> getData(String key, String value, TimeUnit timeunit, int amount, String timespan);
	public List<T> getDetailData(String key, String value, TimeUnit timeunit, int amount);
	public void setData(Share data, Long userId, String flag);

}
