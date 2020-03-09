package com.giant.mindplates.biz.topic.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giant.mindplates.biz.topic.service.TopicService;
import com.giant.mindplates.biz.topic.vo.Topic;
import com.giant.mindplates.biz.topic.vo.request.CreateTopicReqeust;
import com.giant.mindplates.biz.topic.vo.response.CreateTopicResponse;
import com.giant.mindplates.biz.topic.vo.response.GetTopicsResponse;
import com.giant.mindplates.biz.user.service.UserService;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @GetMapping("/name")
    public Boolean checkName(@RequestParam Long organizationId, @RequestParam String name) {
        return topicService.checkName(organizationId, name);
    }

    @PostMapping("")
    public CreateTopicResponse create(@RequestBody CreateTopicReqeust createTopicRequest) {
    	Topic topic = topicService.createTopic(createTopicRequest);
    	
    	fireCreateTopic(topic);

        Link link = new Link("/topics", "topics");

        return CreateTopicResponse.builder()
                .build()
                .add(link);

    }
    
    public void fireCreateTopic(Topic topic) {
    	simpMessagingTemplate.convertAndSend("/sub/topic", topic);
    }

    @GetMapping("")
    public GetTopicsResponse getTopics() {
        return topicService.selectTopicList();
    }

    @GetMapping("/{topicId}")
    public Map getTopic(@PathVariable Long topicId) {
        Map<String, Object> info = new HashMap<>();
        // TODO 사용자가 토픽을 조회할 수 있느 권한 (토픽이 프라이빗이면 본인만, 토픽이 퍼블릿이면 토픽의 ORG에 지금 사용자가 포함되어 있는지 확인)
        info.put("topic", topicService.selectTopic(topicId));
        info.put("topicUsers", userService.selectTopicUserList(topicId));
        return info;
    }

}
