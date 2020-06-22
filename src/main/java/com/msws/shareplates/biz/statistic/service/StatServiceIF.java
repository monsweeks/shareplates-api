package com.msws.shareplates.biz.statistic.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.msws.shareplates.biz.statistic.enums.Stat_database;

public interface StatServiceIF<T> {
	
	public Stat_database getName();
	public List<T> getData(String key, TimeUnit timeunit, int value);
	public List<T> getData(TimeUnit timeunit, int value);
	public void setData(Object data);
	public void setData(Object data, Long userId);

}
