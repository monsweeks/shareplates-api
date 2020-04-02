package com.msws.shareplates.biz.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msws.shareplates.common.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SlackService {

    private String url = "https://hooks.slack.com/services/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";
    @Autowired
    private HttpRequestUtil httpRequestUtil;

    public void sendText(String message) {
        Map<String, String> data = new HashMap<>();
        data.put("text", message);
        ObjectMapper mapper = new ObjectMapper();

        try {
            httpRequestUtil.sendPost(this.url, mapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

}
