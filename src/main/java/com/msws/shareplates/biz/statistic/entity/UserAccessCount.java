package com.msws.shareplates.biz.statistic.entity;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Measurement(name = "test")
public class UserAccessCount {
	
	@Column(name = "socketCnt")
	private Double socketCnt;
	
	@Column(name = "pv")
	private Double pv;
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "shareId", tag = true )
	private String shareId;
	
	@Column(name = "topicId", tag = true )
	private String topicId;
	
	@Column(name = "chapterId", tag = true )
	private String chapterId;
	
	@Column(name = "pageId", tag = true )
	private String pageId;
	
	@Column(name = "joinId", tag = true )
	private String joinId;
	
	@Column(name = "adminUserEmail", tag = true )
	private String 	adminUserEmail;
	


	
}