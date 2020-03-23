package com.msws.shareplates.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "commonAuditorAware")
@Configuration
public class AuditingConfig {

}
