package com.msws.shareplates.framework.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.framework.session.vo.UserInfo;

@Aspect
@Component
public class TopicAuthAspect {
	
	@Autowired
	private AuthService authService;
	
	@Pointcut("@target(com.msws.shareplates.framework.aop.annotation.CheckTopicAuth) && execution(* com.msws.shareplates..create*(..))")
	private void createOperator() {};
	
	@Pointcut("@target(com.msws.shareplates.framework.aop.annotation.CheckTopicAuth) && execution(* com.msws.shareplates..update*(..))")
	private void chapterUpdateOperator() {};
	
	@Pointcut("@target(com.msws.shareplates.framework.aop.annotation.CheckTopicAuth) && execution(* com.msws.shareplates..delete*(..))")
	private void deleteOperator() {};

	@Pointcut("@target(com.msws.shareplates.framework.aop.annotation.CheckTopicAuth) && execution(* com.msws.shareplates..select*(..))")
	private void selectOperator() {};
	
	@Pointcut("createOperator() || chapterUpdateOperator() || selectOperator()")
	private void cudOperator() {};

	@Before("cudOperator() && args(topicId, .., userInfo)")
	public void checkUserHasWriteRoleAboutTopic(JoinPoint joinPoint, long topicId, UserInfo userInfo) throws Throwable {

		if(userInfo.getRoleCode() != RoleCode.SUPER_MAN)
			authService.checkUserHasWriteRoleAboutTopic(topicId, userInfo.getId());
	}

	@Before("selectOperator() && args(topicId, .., userInfo)")
	public void checkUserHasReadRoleAboutTopic(JoinPoint joinPoint, long topicId, UserInfo userInfo) throws Throwable {
		
		if(userInfo.getRoleCode() != RoleCode.SUPER_MAN)
		 authService.checkUserHasReadRoleAboutTopic(topicId, userInfo.getId());
	}
}
