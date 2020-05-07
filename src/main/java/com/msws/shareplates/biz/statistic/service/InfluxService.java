package com.msws.shareplates.biz.statistic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InfluxService implements StatServiceIF<UserAccessCount>{
	
	private final InfluxDBResultMapper resultMapper;
	
	private final String TABLE_NAME = "test";
	
	@Autowired
	private InfluxDBTemplate<Point> influxDBTemplate;
	
	private final String QUERY_BASE = "SELECT * FROM %s LIMIT 1000";
	
	
	public InfluxService() {
		resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
		 
	}
	
	@Override
	public Stat_database getName() {
		return Stat_database.influxdb;				
	}	

	@Override
	public void setData(Object data) {
				
		Map<String, Object> field = new HashMap<String, Object>();
		field.put("count", 1);
		Point insert_point = Point.measurement(TABLE_NAME)
				  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				  .fields(field)
				  .tag("name", data.toString())
				  .build();
		
		influxDBTemplate.write(insert_point);

	}

	@Override
	public List<UserAccessCount> getData() {
		String revised_query = String.format(QUERY_BASE, TABLE_NAME);
		log.error("query {}", revised_query);
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase("stat")
		        .create();

		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, UserAccessCount.class);
	}

}
