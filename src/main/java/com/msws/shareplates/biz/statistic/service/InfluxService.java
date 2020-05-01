package com.msws.shareplates.biz.statistic.service;

import java.util.List;

import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.statistic.entity.H2oFeet;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

@Service
public class InfluxService implements StatServiceIF<H2oFeet>{
	
	private final InfluxDBResultMapper resultMapper;
	
	public InfluxService() {
		resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
	}
	
	@Autowired
	private InfluxDBTemplate<Point> influxDBTemplate;
	
	@Override
	public Stat_database getName() {
		return Stat_database.influxdb;				
	}

	public List<H2oFeet> getData() {
		Query query = QueryBuilder.newQuery("SELECT * FROM h2o_feet LIMIT 1000")
		        .forDatabase("NOAA_water_database")
		        .create();

		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, H2oFeet.class);
	}

	@Override
	public void setData(Object data) {

	}

	

}
