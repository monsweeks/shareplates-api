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
	
	@Pointcut("execution(* com.msws.shareplates..controller.ChapterController.create*(..))")
	private void chapterCreateOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates..controller.ChapterController.update*(..))")
	private void chapterUpdateOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates..controller.ChapterController.delete*(..))")
	private void chapterDeleteOperator() {};

	@Pointcut("execution(* com.msws.shareplates..controller.ChapterController.select*(..))")
	private void chapterSelectOperator() {};
	
	@Pointcut("chapterCreateOperator() || chapterUpdateOperator() || chapterDeleteOperator()")
	private void chapterCUDOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates..controller.PageController.create*(..))")
	private void pageCreateOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates..controller.PageController.update*(..))")
	private void pageUpdateOperator() {};
	
	@Pointcut("execution(* com.msws.shareplates..controller.PageController.delete*(..))")
	private void pageDeleteOperator() {};

	@Pointcut("execution(* com.msws.shareplates..controller.PageController.select*(..))")
	private void pageSelectOperator() {};
	
	@Pointcut("pageCreateOperator() || pageUpdateOperator() || pageDeleteOperator()")
	private void pageCUDOperator() {};

	@Before("(pageCUDOperator() || chapterCUDOperator()) && args(topicId, .., userInfo)")
	public void checkUserHasReadRoleAboutTopic(JoinPoint joinPoint, long topicId, UserInfo userInfo) throws Throwable {

		 authService.checkUserHasWriteRoleAboutTopic(topicId, userInfo.getId());
	}

	@Before("(pageSelectOperator() || chapterSelectOperator()) && args(topicId, .., userInfo)")
	public void checkUserHasWriteRoleAboutTopic(JoinPoint joinPoint, long topicId, UserInfo userInfo) throws Throwable {

		 authService.checkUserHasReadRoleAboutTopic(topicId, userInfo.getId());
	}
}
