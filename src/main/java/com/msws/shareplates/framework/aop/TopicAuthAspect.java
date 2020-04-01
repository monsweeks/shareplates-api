package com.msws.shareplates.framework.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.framework.session.vo.UserInfo;

@Aspect
@Component
public class TopicAuthAspect {
	
	@Autowired
	private AuthService authService;
	
	@Pointcut("execution(* com.msws.shareplates.biz.chapter.controller.ChapterController.create*(..))")
	private void createOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates.biz.chapter.controller.ChapterController.update*(..))")
	private void updateOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates.biz.chapter.controller.ChapterController.delete*(..))")
	private void deleteOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates.biz.chapter.controller.ChapterController.get*(..))")
	private void getOperator() {};

	@Before("(createOperator() || updateOperator() || deleteOperator()) && args(topicId, .., userInfo)")
	public void checkUserHasReadRoleAboutTopic(JoinPoint joinPoint, long topicId, UserInfo userInfo) throws Throwable {

		 authService.checkUserHasWriteRoleAboutTopic(topicId, userInfo.getId());
	}

	@Before("getOperator() && args(topicId, .., userInfo)")
	public void checkUserHasWriteRoleAboutTopic(JoinPoint joinPoint, long topicId, UserInfo userInfo) throws Throwable {

		 authService.checkUserHasReadRoleAboutTopic(topicId, userInfo.getId());
	}
}
