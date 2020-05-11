package com.msws.shareplates.framework.aop;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.page.vo.response.PageResponse;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.biz.statistic.service.StatServiceIF;
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

	@AfterReturning(value = "execution(* com.msws.shareplates..controller.ShareContentsHttpController.selectShareContent(..))", returning = "retVal")
	public void logShareInfo(Object retVal) throws Throwable {
		
		log.info("select topic : {}", retVal);
		
		ShareInfo data = (ShareInfo)retVal;
		mainService.setData(data);
	}

	
	
}
