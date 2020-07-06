package com.msws.shareplates.framework.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.common.bean.InitService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.converter.StringToOAuthVendorConverter;
import com.msws.shareplates.framework.interceptor.LoginCheckInterceptor;
import com.msws.shareplates.framework.resolver.MethodArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	SessionUtil sessionUtil;
	@Autowired
	MessageSourceAccessor messageSourceAccessor;
	@Autowired
	GrpService grpService;
	@Value("${spring.profiles.active}")
	private String activeProfile;
	@Value("${shareplates.corsUrls}")
	private String[] corsUrls;

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {

		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();

		source.setBasename("classpath:/messages/message");
		source.setDefaultEncoding("UTF-8");
		source.setCacheSeconds(60);
		source.setUseCodeAsDefaultMessage(true);
		return source;

	}

	@Bean
	public SessionLocaleResolver localeResolver() {
		return new SessionLocaleResolver();

	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource());
		return messageSourceAccessor;
	}

	@Bean
	public InitService initService() {
		InitService initService = new InitService(grpService);
		initService.init();
		return initService;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(this.corsUrls)
				.allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS").allowCredentials(true);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		
		registry.addInterceptor(
				new LoginCheckInterceptor(this.sessionUtil, this.messageSourceAccessor, this.activeProfile))
				.addPathPatterns("/**")
				.excludePathPatterns("/test/**/")
				.excludePathPatterns("/v3/**")
				.excludePathPatterns("/webjars/**")
				.excludePathPatterns("/swagger-ui/**")
				.excludePathPatterns("/swagger**")
				.excludePathPatterns("/swagger-resources/**")
				.excludePathPatterns("/error");
	}

	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new MethodArgumentResolver());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToOAuthVendorConverter());
	}

}
