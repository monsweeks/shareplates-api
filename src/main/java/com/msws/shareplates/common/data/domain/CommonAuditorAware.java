package com.msws.shareplates.common.data.domain;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.msws.shareplates.common.util.SessionUtil;

@Component
public class CommonAuditorAware implements AuditorAware<Long>{

	@Override
	public Optional<Long> getCurrentAuditor() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
			return Optional.ofNullable(SessionUtil.getUserId(request));
		}

		return Optional.ofNullable(0L);

	}

}
