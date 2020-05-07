package com.msws.shareplates.framework.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

	@Around("execution(* com.msws.shareplates..service.*.getData(..))")
	public void checkelapsedTime(ProceedingJoinPoint pjp) throws Throwable {
		log.info("start - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        pjp.proceed();
        log.info("finished - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
	}

	
	
}
