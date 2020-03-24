package com.msws.shareplates.framework.config;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateConfig {

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {{
            setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder
                    .create()
                    .setConnectionManager(new PoolingHttpClientConnectionManager() {{
                       setDefaultMaxPerRoute(600);// 각 HOST (IP + PORT) 당 Connection Pool 에 생성가능한 Connection 의 수
                       setMaxTotal(1800); // Connection Pool 의 수용 가능한 최대 사이즈
                    }})
                    .setConnectionTimeToLive(10, TimeUnit.SECONDS)
                    .build()));
        }};
    }
	
 

}
