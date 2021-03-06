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
public class GrpAuthAspect {

	@Autowired
	private AuthService authService;

	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth)) && execution(* com.msws.shareplates..create*(..))")
	private void createOperator() {};

	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth)) && execution(* com.msws.shareplates..update*(..))")
	private void updateOperator() {};

	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth)) && execution(* com.msws.shareplates..delete*(..))")
	private void deleteOperator() {};

	@Pointcut("(@target(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.CheckGrpAuth)) && !(@target(com.msws.shareplates.framework.aop.annotation.WriteAuth) || @annotation(com.msws.shareplates.framework.aop.annotation.WriteAuth)) && execution(* com.msws.shareplates..select*(..))")
	private void selectOperator() {
	}

	@Pointcut("createOperator() || updateOperator() || deleteOperator()")
	private void cudOperator() {};

	@Before("cudOperator() && args(grpId, .., userInfo)")
	public void checkUserHasWriteRoleAboutGrp(JoinPoint joinPoint, long grpId, UserInfo userInfo) throws Throwable {
		if(RoleCode.SUPER_MAN != userInfo.getRoleCode()) {
			authService.checkUserHasWriteRoleAboutGrp(grpId, userInfo.getId());
		}
	}

	@Before("selectOperator() && args(grpId, .., userInfo)")
	public void checkUserHasReadRoleAboutGrp(JoinPoint joinPoint, long grpId, UserInfo userInfo) throws Throwable {
		if (userInfo == null || RoleCode.SUPER_MAN != userInfo.getRoleCode()) {
            Long userId = userInfo != null ? userInfo.getId() : null;
            authService.checkUserHasReadRoleAboutGrp(grpId, userId);
        }

	}
}
