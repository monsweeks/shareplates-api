package com.giant.mindplates.biz.topic.controller;

import com.giant.mindplates.biz.topic.service.TopicService;
import com.giant.mindplates.common.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @Autowired
    SessionUtil sessionUtil;

    @GetMapping("/name")
    public Boolean checkName(@RequestParam Long organizationId, @RequestParam String name) {
        return topicService.checkName(organizationId, name);
    }

    @PostMapping("")
    public void create() {

        // TODO 양일동

    }

}
