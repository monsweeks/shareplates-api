package com.msws.shareplates.framework.aop;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.biz.statistic.service.StatServiceIF;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.common.exception.StatDBException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class StatisticAspect {
	
	private final StatServiceIF<?> mainService;

	@Autowired
	public StatisticAspect(List<StatServiceIF<?>> services, @Value("${stat.database}") Stat_database database) {
		
		this.mainService = services.stream().filter( e -> e.getName() == database)
				.findFirst()
				.orElseThrow( () ->
						new StatDBException("no stat database selected")
		);
 
	}

	@AfterReturning(value = "execution(* com.msws.shareplates..service.*.selectTopic(..))", returning = "retVal")
	public void selectTopic(Object retVal) throws Throwable {
		
		log.info("select topic : {}", retVal);
		
		Topic data = (Topic)retVal;
		mainService.setData(data.getId());
	}
	
	@AfterReturning(value = "execution(* com.msws.shareplates..service.*.selectChapter(..))", returning = "retVal")
	public void selectChapter(Object retVal) throws Throwable {
		
		log.info("select chapter : {}", retVal);
		
		Chapter data = (Chapter)retVal;
		mainService.setData(data.getId());
	}
	
	@AfterReturning(value = "execution(* com.msws.shareplates..service.*.selectPage(..))", returning = "retVal")
	public void selectPage(Object retVal) throws Throwable {
		
		log.info("select page : {}", retVal);
		
		Page data = (Page)retVal;
		mainService.setData(data.getId());
		
	}
	
	

	
	
}
