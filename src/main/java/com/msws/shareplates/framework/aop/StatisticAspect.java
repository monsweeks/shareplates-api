package com.msws.shareplates.framework.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
@Component
public class StatisticAspect {
	
	@Autowired
	private StatService statService;
	
	@Autowired
	private SessionUtil sessionUtil;
	
	@Autowired
    private ShareService shareService;

	@AfterReturning(value = "execution(* com.msws.shareplates..controller.ShareContentsHttpController.selectShareContent(..))", returning = "retVal")
	public void logShareInfo(Object retVal) throws Throwable {
		
		try {
			
			HttpServletRequest request = 
			        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			
			ShareInfo data = (ShareInfo)retVal;
			statService.writeJoinData(shareService.selectShare(data.getShare().getId()), sessionUtil.getUserId(request));
		}catch(Exception e) {
			log.error("fail to write statistical data : {}", e.getMessage());
		}

	}
	
	@AfterReturning(value = "execution(* com.msws.shareplates..controller.ShareContentsHttpController.updateCurrentPosition(..)) && args(shareId, .., userInfo)", returning = "retVal")
	public void logShareResponse(long shareId, UserInfo userInfo, Object retVal) throws Throwable {
		
		try {
			
			HttpServletRequest request = 
			        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			
	        Share share = shareService.selectShare(shareId);
	        statService.writeJoinData(share, sessionUtil.getUserId(request));			
		}catch(Exception e) {
			log.error("fail to write statistical data : {}", e.getMessage());
		}

	}
	
	

	
	
}
