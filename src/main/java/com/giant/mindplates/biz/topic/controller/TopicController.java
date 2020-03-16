package com.giant.mindplates.biz.topic.controller;

import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.biz.topic.entity.Topic;
import com.giant.mindplates.biz.topic.service.TopicService;
import com.giant.mindplates.biz.topic.vo.SimpleTopic;
import com.giant.mindplates.biz.topic.vo.request.TopicRequest;
import com.giant.mindplates.biz.topic.vo.response.TopicResponse;
import com.giant.mindplates.biz.topic.vo.response.TopicsResponse;
import com.giant.mindplates.biz.user.service.UserService;
import com.giant.mindplates.common.code.StatusCode;
import com.giant.mindplates.common.util.SessionUtil;
import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.framework.session.vo.UserInfo;
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
    OrganizationService organizationService;



    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void pubTopic(SimpleTopic simpleTopic) {
        simpMessagingTemplate.convertAndSend("/sub/simpleTopic", simpleTopic);
    }

    @GetMapping("/exist")
    public Boolean selectTopicNameExist(@RequestParam Long organizationId, @RequestParam String name) {
        return topicService.selectIsTopicNameExist(organizationId, name);
    }

    @DisableLogin
    @GetMapping("")
    public TopicsResponse selectTopicList(@RequestParam Long organizationId, @RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, HttpServletRequest request) {
        Long userId = SessionUtil.getUserId(request);
        organizationService.checkOrgIncludesUser(organizationId, userId);
        return new TopicsResponse(topicService.selectTopicList(userId, organizationId, searchWord, order, direction));
    }

    @GetMapping("/{topicId}")
    public TopicResponse selectTopic(@PathVariable Long topicId, UserInfo userInfo) {
        topicService.checkUserHasTopicReadRole(topicId, userInfo.getId());
        return new TopicResponse(topicService.selectTopic(topicId));
    }

    @PostMapping("")
    public TopicResponse createTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        organizationService.checkOrgIncludesUser(topicRequest.getOrganizationId(), userInfo.getId());
        Topic topic = topicService.createTopic(new Topic(topicRequest));
        SimpleTopic simpleTopic = new SimpleTopic(topic, StatusCode.CREATE);
        pubTopic(simpleTopic);
        Link link = new Link("/topics", "topics");
        return TopicResponse.builder().build().add(link);
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
