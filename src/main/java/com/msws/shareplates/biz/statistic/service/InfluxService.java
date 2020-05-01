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

import com.msws.shareplates.biz.statistic.entity.Influx_test;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

@Service
public class InfluxService implements StatServiceIF<Influx_test>{
	
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

	public List<Influx_test> getData() {
		Query query = QueryBuilder.newQuery("SELECT * FROM test LIMIT 1000")
		        .forDatabase("stat")
		        .create();

		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, Influx_test.class);
	}

	@Override
	public void setData(Object data) {

	}

	

}
