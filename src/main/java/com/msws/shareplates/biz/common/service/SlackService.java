package com.msws.shareplates.biz.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.common.vo.SlackMessage;
import com.msws.shareplates.common.util.HttpRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SlackService {

	@Value("${slack.url}")
    private String url;
	
    @Autowired
    private HttpRequestUtil httpRequestUtil;

    public void sendText(String message) {        

        httpRequestUtil.sendPost(this.url, SlackMessage.builder().text(message).build(), SlackMessage.class);
    }

}
