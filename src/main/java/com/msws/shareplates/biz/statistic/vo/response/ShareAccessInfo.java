package com.msws.shareplates.biz.statistic.vo.response;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Measurement(name = "test")
public class ShareAccessInfo {
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "sessionCnt")
	private Double sessionCnt = 0D;
	
	@Column(name = "userCnt")
	private Double userCnt = 0D;
	
	@Column(name = "focusCnt")
	private Double focusCnt = 0D;
}
