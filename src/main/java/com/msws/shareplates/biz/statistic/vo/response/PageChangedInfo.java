package com.msws.shareplates.biz.statistic.vo.response;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Measurement(name = "test")
public class PageChangedInfo {
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "chapterId", tag = true )
	private String chapterId;
	
	@Column(name = "pageId", tag = true )
	private String pageId;

}
