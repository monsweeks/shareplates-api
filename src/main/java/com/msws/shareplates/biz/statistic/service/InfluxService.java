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

import com.msws.shareplates.biz.share.entity.Share;
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
	
	private final String QUERY_BASE = "SELECT %s FROM %s where time > %s %s GROUP BY time(1m),joinId,shareId order by time asc";
	private final String QUERY_DETAIL_BASE = "SELECT * FROM %s where time > %s %s";
	
	
	public InfluxService() {
		resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
		 
	}
	
	@Override
	public Stat_database getName() {
		return Stat_database.influxdb;				
	}	
	
	@Override
	public void setData(Share data, Long userId, String flag) {
		
		Map<String, String> tags = new HashMap<String, String>();
		Map<String, Object> field = new HashMap<String, Object>();

		tags.put("shareId", data.getId().toString());
		tags.put("topicId", data.getTopic().getId().toString());
		tags.put("chapterId", data.getCurrentChapter().getId().toString());
		tags.put("pageId", data.getCurrentPage().getId().toString());
		tags.put("adminUserEmail", data.getAdminUser().getEmail());
		tags.put("joinId", userId.toString());
		
		switch(flag) {
				
			case "join" :
		
					for(String each : FIELD_NAME.split(",")) {
						
						switch(each.trim()) {
						case "pv" :
							//field.put( each.trim(), tempShareData.getShareUsers().stream().filter(e -> RoleCode.MEMBER.equals(e.getRole()) && SocketStatusCode.ONLINE.equals(e.getStatus())).count());
							field.put( each.trim(), 1);
							break;
						case "socketCnt" :

							field.put(each.trim(), 1);
							break;
						
						default :
								break;
						}
					}
					
					
					break;
					
			case "out" :
	
				for(String each : FIELD_NAME.split(",")) {
					
					switch(each.trim()) {
					case "pv" :
						field.put( each.trim(), -1);
						break;
					case "socketCnt" :
						field.put(each.trim(), -1);
						break;
					
					default :
							break;
					}
				}
				
				
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
	
	public List<UserAccessCount> getData(String key, String value, TimeUnit timeunit, int amount) {
		
		if( key == null || key.isEmpty()) {
			key = "";
		}else {
			key = String.format("and %s = '%s'", key, value);
		}
		
		//SELECT %s FROM %s where time > %s %s GROUP BY time(1m) order by time asc
		String revised_query = String.format(QUERY_BASE, getCountFieldSentence(), TABLE_NAME, getTimeStamp(timeunit, amount) , key);
		log.error("query : {}", revised_query);
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase("stat")
		        .create();

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
		String revised_query = String.format(QUERY_BASE, TABLE_NAME, getTimeStamp(timeunit, amount) , key);
		log.error("query : {}", revised_query);
		Query query = QueryBuilder.newQuery(revised_query)
		        .forDatabase("stat")
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


}
