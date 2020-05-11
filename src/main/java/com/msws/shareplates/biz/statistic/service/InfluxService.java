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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InfluxService implements StatServiceIF<UserAccessCount>{
	
	private final InfluxDBResultMapper resultMapper;
	
	
	@Value("${stat.table}")
	private String TABLE_NAME;
	
	@Value("${stat.key}")
	private String TAG_NAME;

	@Value("${stat.value}")
	private String FIELD_NAME;
	
	@Autowired
	private InfluxDBTemplate<Point> influxDBTemplate;
	
	private final String QUERY_BASE = "SELECT count(%s) as count FROM %s where time > %s %s group by \"%s\"";
	
	
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
		field.put(FIELD_NAME, 1);
		Point insert_point = Point.measurement(TABLE_NAME)
				  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				  .fields(field)
				  .tag(TAG_NAME, data.toString())
				  .build();
		
		influxDBTemplate.write(insert_point);

	}
	
	@Override
	public List<UserAccessCount> getData(TimeUnit timeunit, int amount) {
		
		String revised_query = String.format(QUERY_BASE, FIELD_NAME, TABLE_NAME, getTimeStamp(timeunit, amount)  , "", TAG_NAME);
		log.error("query : {}", revised_query);
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase("stat")
		        .create();

		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, UserAccessCount.class);

	}


	@Override
	public List<UserAccessCount> getData(String key, TimeUnit timeunit, int amount) {
		
		key = String.format("and %s = '%s'", TAG_NAME, key);
		String revised_query = String.format(QUERY_BASE, FIELD_NAME, TABLE_NAME, getTimeStamp(timeunit, amount) , key, TAG_NAME);
		log.error("query : {}", revised_query);
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase("stat")
		        .create();

		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, UserAccessCount.class);
	}
	
	private String getTimeStamp(TimeUnit timeunit, int amount) {
		
		String time_query = "";
		switch(timeunit) {
		case DAYS :
			time_query = "now() - " + String.valueOf(amount) + "d";
			break;
		case HOURS :
			time_query = "now() - " + String.valueOf(amount) + "h";
			break;
		case MINUTES : 
			time_query = "now() - " + String.valueOf(amount) + "m";
			break;
		default :
			time_query = "now() - " + String.valueOf(amount) + "d";
			break;
		}
		
		return time_query;
		
	}


}
