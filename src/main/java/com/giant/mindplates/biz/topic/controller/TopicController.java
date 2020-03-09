package com.giant.mindplates.biz.topic.controller;

import com.giant.mindplates.biz.topic.entity.Topic;
import com.giant.mindplates.biz.topic.service.TopicService;
import com.giant.mindplates.biz.topic.vo.request.CreateTopicReqeust;
import com.giant.mindplates.biz.topic.vo.response.CreateTopicResponse;
import com.giant.mindplates.biz.topic.vo.response.GetTopicsResponse;
import com.giant.mindplates.biz.user.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Log
@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

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
    public GetTopicsResponse getTopics(@RequestParam Long organizationId, @RequestParam String searchWord, @RequestParam String order, @RequestParam String direction) {
        log.info(organizationId.toString());
        log.info(searchWord);
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

    @DeleteMapping("/{topicId}")
    public void deleteTopic(@PathVariable Long topicId ) {
        topicService.deleteTopic(topicId);
    }

}
