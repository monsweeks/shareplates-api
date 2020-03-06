package com.giant.mindplates.biz.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giant.mindplates.biz.topic.service.TopicService;
import com.giant.mindplates.biz.topic.vo.request.CreateTopicReqeust;
import com.giant.mindplates.biz.topic.vo.response.CreateTopicResponse;
import com.giant.mindplates.biz.topic.vo.response.GetTopicsResponse;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @GetMapping("/name")
    public Boolean checkName(@RequestParam Long organizationId, @RequestParam String name) {
        return topicService.checkName(organizationId, name);
    }

    @PostMapping("")
    public CreateTopicResponse create(@RequestBody CreateTopicReqeust createTopicRequest) {
    	topicService.createTopic(createTopicRequest);
    	
    	Link link = new Link("/topics", "topics");
    	
    	return CreateTopicResponse.builder()
    			.build()
    			.add(link);

    }
    
    @GetMapping("")
    public GetTopicsResponse getTopics() {
    	return topicService.selectTopicList();
    }

}
