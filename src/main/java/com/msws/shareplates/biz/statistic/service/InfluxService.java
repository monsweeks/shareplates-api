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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
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
	
	private final String QUERY_BASE = "SELECT %s as count FROM %s where time > %s %s group by \"%s\"";
	
	
	public InfluxService() {
		resultMapper = new InfluxDBResultMapper(); // thread-safe - can be reused
		 
	}
	
	@Override
	public Stat_database getName() {
		return Stat_database.influxdb;				
	}	

	@Override
	public void setData(Object data) {
		
	}
	
	@Async
	@Override
	public void setData(Object data, Long userId) {
		
		Map<String, String> tags = new HashMap<String, String>();
		Map<String, Object> field = new HashMap<String, Object>();			


		
		switch(data.getClass().getSimpleName().toLowerCase()) {
		case "share" :
			Share tempShareData = (Share) data;
			
			tags.put("shareId", tempShareData.getId().toString());
			tags.put("topicId", tempShareData.getTopic().getId().toString());
			tags.put("chapterId", tempShareData.getCurrentChapter().getId().toString());
			tags.put("pageId", tempShareData.getCurrentPage().getId().toString());
			tags.put("adminUserEmail", tempShareData.getAdminUser().getEmail());
			tags.put("joinId", userId.toString());

			for(String each : FIELD_NAME.split(",")) {
				
				switch(each.trim()) {
				case "pv" :
					//field.put( each.trim(), tempShareData.getShareUsers().stream().filter(e -> RoleCode.MEMBER.equals(e.getRole()) && SocketStatusCode.ONLINE.equals(e.getStatus())).count());
					field.put( each.trim(), 1);
					break;
				case "socketCnt" :
					
					//int socketCnt = 0;
					//for( ShareUser shareuser : tempShareData.getShareUsers()) {
					//	socketCnt += shareuser.getShareUserSocketList().size();						
					//}
					ShareUser shareuser = tempShareData.getShareUsers().stream()
											.filter(e -> e.getId() == userId).findFirst().orElse(null);
					field.put(each.trim(), shareuser == null ? 1 : shareuser.getShareUserSocketList().size());
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
	
	@Override
	public List<UserAccessCount> getData(TimeUnit timeunit, int amount) {
		
		
		String revised_query = String.format(QUERY_BASE, getCountFieldSentence(), TABLE_NAME, getTimeStamp(timeunit, amount)  , "", TAG_NAME);
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
		String revised_query = String.format(QUERY_BASE, getCountFieldSentence(), TABLE_NAME, getTimeStamp(timeunit, amount) , key, TAG_NAME);
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
			count_field.append(String.format("count(%s)", each));
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
