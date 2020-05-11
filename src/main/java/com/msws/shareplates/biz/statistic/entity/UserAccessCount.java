package com.msws.shareplates.biz.statistic.entity;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Measurement(name = "test")
public class UserAccessCount {
	
	@Column(name = "count")
	private Double count;
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "object", tag = true )
	private String object;

	
}