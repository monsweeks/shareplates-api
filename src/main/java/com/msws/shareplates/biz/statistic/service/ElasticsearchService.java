package com.msws.shareplates.biz.statistic.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.MDC;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.statistic.enums.Stat_database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ElasticsearchService implements StatServiceIF<Object> {

	@Override
	public Stat_database getName() {
		return Stat_database.elasticsearch;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void setData(Object data, String index_name) {
		
		MDC.put("index_name", index_name);
		if( data instanceof List) {
			for( Object row : (List<Object>) data) {
				log.info("{}", new Gson().toJson(row));
			}
		}else {
			log.info("{}", new Gson().toJson(data));
		}
		MDC.clear();
	}



	@Override
	public void setData(Share data, Long userId, String flag, Object additional_value) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List<Object> getDetailData(String value, Timestamp from, Timestamp to) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Object> getData(String value, Timestamp from, Timestamp to) {
		// TODO Auto-generated method stub
		return null;
	}

}
