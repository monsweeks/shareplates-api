package com.msws.shareplates.framework.aop;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.biz.statistic.service.StatServiceIF;
import com.msws.shareplates.common.exception.StatDBException;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
@Component
public class StatisticAspect {
	
	private final StatServiceIF<?> mainService;
	
	@Autowired
    private ShareService shareService;

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
		
		try {
			ShareInfo data = (ShareInfo)retVal;
			mainService.setData(shareService.selectShare(data.getShare().getId()));
		}catch(Exception e) {
			log.error("fail to write statistical data : {}", e.getMessage());
		}

	}
	
	@AfterReturning(value = "execution(* com.msws.shareplates..controller.ShareContentsHttpController.updateCurrentPosition(..)) && args(shareId, .., userInfo)", returning = "retVal")
	public void logShareResponse(long shareId, UserInfo userInfo, Object retVal) throws Throwable {
		
		try {
	        Share share = shareService.selectShare(shareId);
			mainService.setData(share);			
		}catch(Exception e) {
			log.error("fail to write statistical data : {}", e.getMessage());
		}

	}
	
	

	
	
}
