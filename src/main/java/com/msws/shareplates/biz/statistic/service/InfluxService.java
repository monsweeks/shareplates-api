package com.msws.shareplates.biz.statistic.service;

import java.sql.Timestamp;
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

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.statistic.entity.UserAccessCount;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InfluxService implements StatServiceIF<UserAccessCount>{
	
	private final InfluxDBResultMapper resultMapper;

	@Value("${stat.databaseName}")
	private String DB_NAME;
	
	@Value("${stat.table}")
	private String TABLE_NAME;
	
	@Value("${stat.key}")
	private String TAG_NAME;

	@Value("${stat.value}")
	private String FIELD_NAME;
	
	
	
	@Autowired
	private InfluxDBTemplate<Point> influxDBTemplate;
	
	private final String QUERY_FROM_TO_BASE = "SELECT %s FROM %s where time >= '%s' AND time <= '%s' AND shareId = %s GROUP BY time(%s),shareId order by time asc";
	private final String QUERY_DETAIL_BASE = "SELECT * FROM %s where time > %s %s";
	
	
	public InfluxService() {
		resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
		 
	}
	
	@Override
	public Stat_database getName() {
		return Stat_database.influxdb;				
	}	
	
	@Override
	public void setData(Share data, Long userId, String flag, Object additional_value) {
		
		Map<String, String> tags = new HashMap<String, String>();
		Map<String, Object> field = new HashMap<String, Object>();

		tags.put("shareId", data.getId().toString());
		tags.put("topicId", data.getTopic().getId().toString());
		tags.put("chapterId", data.getCurrentChapter().getId().toString());
		tags.put("pageId", data.getCurrentPage().getId().toString());
		tags.put("adminUserEmail", data.getAdminUser().getEmail());
		tags.put("userId", userId.toString());
		
		switch(flag) {
				
			case "join" :
					field.put( "sessionCnt", 1);
					field.put( "pageChangedCnt", 0);
					break;

			case "out" :
					field.put( "sessionCnt", -1);
					field.put( "pageChangedCnt", 0);
				break;
			
			case "page_changed":
					field.put( "pageChangedCnt", 1);
					tags.put("pageChanged", "true");
				break;
				
			case "focus_changed":
					field.put( "focusChangedCnt", 1);
					field.put( "pageChangedCnt", 0);
					tags.put("focusChanged", additional_value == null ? "DN" : additional_value.toString());
				break;
			default :
					tags.put("shareId", "DN");  // dont know
					break;
		}
				


		Point insert_point = Point.measurement(TABLE_NAME)
				  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				  .fields(field)
				  .tag(tags)
				  .build();
		
		influxDBTemplate.write(insert_point);

	}
	
	public List<UserAccessCount> getData(String shareId, Timestamp from , Timestamp to) {
		
		//"SELECT %s FROM %s where time >= %s AND time <= %s AND shareId = %s GROUP BY time(%s),joinId,shareId order by time asc";
		String revised_query = String.format(QUERY_FROM_TO_BASE, getCountFieldSentence(), TABLE_NAME, 
											from, to, shareId,
											calculateTimeSpan(from , to));
		
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase(DB_NAME)
		        .create();
		log.error("sql {}",revised_query );
		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, UserAccessCount.class);
	}
	
	
	
	public List<UserAccessCount> getDetailData(String key, String value, TimeUnit timeunit, int amount) {
		
		if( key == null || key.isEmpty()) {
			key = "";
		}else {
			key = String.format("and %s = '%s'", key, value);
		}
		
		//SELECT * FROM %s where time > %s %s
		String revised_query = String.format(QUERY_DETAIL_BASE, TABLE_NAME, getTimeStamp(timeunit, amount) , key);
		log.error("query : {}", revised_query);
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase(DB_NAME)
		        .create();
		
		QueryResult queryResult = influxDBTemplate.query(query);
		return resultMapper.toPOJO(queryResult, UserAccessCount.class);
	}

	private String getCountFieldSentence() {
		
		StringBuilder count_field = new StringBuilder();
		
		String prefix = "";
		for(String each : FIELD_NAME.split(",")) {
			count_field.append(prefix);
			prefix = ",";
			count_field.append(String.format("sum(%s) as %s", each, each));
		}

		return count_field.toString();
		
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
	
	private String calculateTimeSpan(Timestamp from, Timestamp to) {
		long gap = to.getTime() - from.getTime();
		int seconds = (int) gap / 1000;
		int minutes =  ( seconds % 3600 ) / 60;
		return minutes < 50 ? "1m" : String.valueOf(minutes / 50);
	}

}
