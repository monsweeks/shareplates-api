package com.msws.shareplates.framework.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.session.vo.UserInfo;

@Aspect
@Component
public class AdminAuthAspect {
	
	@Autowired
	private HttpServletRequest request;

	@Before("@annotation(com.msws.shareplates.framework.aop.annotation.AdminOnly)")
	public void checkAdmin(JoinPoint joinPoint) throws Throwable {
		UserInfo userInfo = SessionUtil.getUserInfo(request);
		
		if(RoleCode.SUPER_MAN != userInfo.getRoleCode()) 
			throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
	}
}
