package com.msws.shareplates.biz.statistic.entity;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Measurement(name = "test")
public class UserAccessCount {
	
	@Column(name = "sessionCnt")
	private Double sessionCnt;
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "pageChangedCnt")
	private Double pageChangedCnt;
	
	@Column(name = "focusChangedCnt")
	private Double focusChangedCnt;
	
	@Column(name = "pageChanged", tag = true)
	private Double pageChanged;
	
	@Column(name = "focusChanged", tag = true)
	private Double focusChanged;
	
	@Column(name = "shareId", tag = true )
	private String shareId;
	
	@Column(name = "topicId", tag = true )
	private String topicId;
	
	@Column(name = "chapterId", tag = true )
	private String chapterId;
	
	@Column(name = "pageId", tag = true )
	private String pageId;
	
	@Column(name = "userId", tag = true )
	private String userId;
	
	@Column(name = "adminUserEmail", tag = true )
	private String 	adminUserEmail;
	


	
}