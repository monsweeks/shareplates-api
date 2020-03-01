package com.giant.mindplates.framework.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.common.bean.InitService;
import com.giant.mindplates.common.util.SessionUtil;
import com.giant.mindplates.framework.interceptor.LoginCheckInterceptor;
import com.giant.mindplates.framework.resolver.MethodArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {


	
	@Value("${spring.profiles.active}")
	private String activeProfile;

    @Value("${shareplates.corsUrl}")
    private String corsUrl;


    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    MessageSourceAccessor messageSourceAccessor;

    @Autowired
    OrganizationService organizationService;

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
        InitService initService = new InitService(organizationService);
        initService.init();
        return initService;
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(this.corsUrl).allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS").allowCredentials(true);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new LoginCheckInterceptor(this.sessionUtil, this.messageSourceAccessor, this.activeProfile))
                .addPathPatterns("/**")
                .excludePathPatterns("/test/**/")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/error");
    }
    
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    	resolvers.add(new MethodArgumentResolver());
    }

}
