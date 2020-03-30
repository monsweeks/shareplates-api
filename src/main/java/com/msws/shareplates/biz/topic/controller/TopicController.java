package com.msws.shareplates.biz.topic.controller;

import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.SimpleTopic;
import com.msws.shareplates.biz.topic.vo.request.TopicRequest;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.topic.vo.response.TopicsResponse;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.common.code.StatusCode;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.session.vo.UserInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Log
@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @Autowired
    UserService userService;

    @Autowired
    GrpService grpService;


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void pubTopic(SimpleTopic simpleTopic) {
        simpMessagingTemplate.convertAndSend("/sub/simpleTopic", simpleTopic);
    }

    @GetMapping("/exist")
    public Boolean selectTopicNameExist(@RequestParam Long grpId, @RequestParam String name) {
        return topicService.selectIsTopicNameExist(grpId, name);
    }

    @DisableLogin
    @GetMapping("")
    public TopicsResponse selectTopicList(@RequestParam Long grpId, @RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, HttpServletRequest request) {
        Long userId = SessionUtil.getUserId(request);
        grpService.checkGrpIncludesUser(grpId, userId);
        return new TopicsResponse(topicService.selectTopicList(userId, grpId, searchWord, order, direction));
    }

    @GetMapping("/{topicId}")
    public TopicResponse selectTopic(@PathVariable Long topicId, UserInfo userInfo) {
        topicService.checkUserHasTopicReadRole(topicId, userInfo.getId());
        return new TopicResponse(topicService.selectTopic(topicId));
    }

    @PostMapping("")
    public TopicResponse createTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        grpService.checkGrpIncludesUser(topicRequest.getGrpId(), userInfo.getId());
        Topic topic = topicService.createTopic(new Topic(topicRequest));
        SimpleTopic simpleTopic = new SimpleTopic(topic, StatusCode.CREATE);
        pubTopic(simpleTopic);
        return new TopicResponse(topic);
    }

    @PutMapping("/{topicId}")
    public TopicResponse updateTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        topicService.checkUserHasTopicWriteRole(topicRequest.getId(), userInfo.getId());
        Topic topic = topicService.updateTopic(new Topic(topicRequest));
        pubTopic(new SimpleTopic(topic, StatusCode.UPDATE));
        Link link = new Link("/topics", "topics");
        return TopicResponse.builder().build().add(link);
    }

    @DeleteMapping("/{topicId}")
    public TopicResponse deleteTopic(@PathVariable Long topicId, UserInfo userInfo) {
        topicService.checkUserHasTopicWriteRole(topicId, userInfo.getId());
        topicService.deleteTopic(topicId);
        Link link = new Link("/topics", "topics");
        return TopicResponse.builder().build().add(link);
    }

}
