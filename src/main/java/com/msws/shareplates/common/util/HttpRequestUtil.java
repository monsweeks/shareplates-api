package com.msws.shareplates.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;


@Slf4j
@Component
public class HttpRequestUtil {

    private static RestTemplate restTemplate;

    @Autowired
    public HttpRequestUtil(RestTemplate restTemplate) {
        HttpRequestUtil.restTemplate = restTemplate;
    }

    public String sendPost(String url, String message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
            restTemplate.postForEntity(url, message, String.class);


        } catch (Exception e) {
            log.error("fail to http Request : {}", e);

        }

        return null;

    }


    public String sendRequest(String url, MultiValueMap<String, String> param, String[] headerK, String[] headerV, HttpMethod httpMethod, MediaType mediaType) {


        try {

            HttpHeaders headers = new HttpHeaders();


            if (headerK != null) {
                int i = 0;
                for (String each : headerK)
                    headers.add(each, headerV[i++]);
            }

            headers.setContentType(mediaType == null ? new MediaType("application", "json", Charset.forName("UTF-8")) : mediaType);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(param, headers);
            return restTemplate.exchange(url, httpMethod, entity, String.class).getBody();

        } catch (Exception e) {
            log.error("fail to http Request : {}", e);

        }

        return null;

    }

}
