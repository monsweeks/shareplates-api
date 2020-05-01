package com.msws.shareplates.biz.statistic.entity;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Measurement(name = "test")
public class Influx_test {
	
	@Column(name = "count")
	private Double count;
	
	@Column(name = "name")
	private String name;

	
}