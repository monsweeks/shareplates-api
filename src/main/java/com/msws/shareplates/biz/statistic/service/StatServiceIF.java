package com.msws.shareplates.biz.statistic.service;

import java.util.List;

import com.msws.shareplates.biz.statistic.enums.Stat_database;

public interface StatServiceIF<T> {
	
	public Stat_database getName();
	public List<T> getData();
	public void setData(Object data);

}
