package com.msws.shareplates.framework.aop;

import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ShareAuthAspect {
	
	@Autowired
	private AuthService authService;
	
	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckShareAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckShareAuth)) && execution(* com.msws.shareplates..create*(..))")
	private void createOperator() {};
	
	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckShareAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckShareAuth)) && execution(* com.msws.shareplates..update*(..))")
	private void updateOperator() {};
	
	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckShareAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckShareAuth)) && execution(* com.msws.shareplates..delete*(..))")
	private void deleteOperator() {};

	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckShareAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckShareAuth)) && !(@target(com.msws.shareplates.framework.aop.annotation.WriteAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.WriteAuth)) && execution(* com.msws.shareplates..select*(..))")
	private void selectOperator() {
	}

	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckShareAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckShareAuth)) && (@target(com.msws.shareplates.framework.aop.annotation.WriteAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.WriteAuth)) && execution(* com.msws.shareplates..select*(..))")
	private void writeOperator() {
	}
	
	@Pointcut("createOperator() || updateOperator() || deleteOperator() || writeOperator()")
	private void cudOperator() {};

	@Before("cudOperator() && args(shareId, .., userInfo)")
	public void checkUserHasWriteRoleAboutShare(JoinPoint joinPoint, long shareId, UserInfo userInfo) throws Throwable {
		if(RoleCode.SUPER_MAN != userInfo.getRoleCode()) {
			authService.checkUserHasWriteRoleAboutShare(shareId, userInfo.getId());
		}
	}

	@Before("selectOperator() && args(shareId, .., userInfo)")
	public void checkUserHasReadRoleAboutTopic(JoinPoint joinPoint, long shareId, UserInfo userInfo) throws Throwable {
		if (RoleCode.SUPER_MAN != userInfo.getRoleCode())
			authService.checkUserHasReadRoleAboutTopicByShareId(shareId, userInfo.getId());
	}
}
