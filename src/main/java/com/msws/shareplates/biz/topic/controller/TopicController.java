package com.msws.shareplates.biz.topic.controller;

import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.SimpleTopic;
import com.msws.shareplates.biz.topic.vo.request.TopicRequest;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.topic.vo.response.TopicsResponse;
import com.msws.shareplates.common.code.StatusCode;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.session.vo.UserInfo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
    AuthService authService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void pubTopic(SimpleTopic simpleTopic) {
        simpMessagingTemplate.convertAndSend("/sub/simpleTopic", simpleTopic);
    }

    @PostMapping("")
    public TopicResponse createTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutGrp(topicRequest.getGrpId(), userInfo.getId());

        Topic topic = topicService.createTopic(new Topic(topicRequest));
        SimpleTopic simpleTopic = new SimpleTopic(topic, StatusCode.CREATE);
        pubTopic(simpleTopic);

        return new TopicResponse(topic);
    }

    @GetMapping("/exist")
    public Boolean selectTopicNameExist(@RequestParam Long grpId, @RequestParam String name, UserInfo userInfo) {
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userInfo.getId());

        return topicService.selectIsTopicNameExist(grpId, name);
    }

    @DisableLogin
    @GetMapping("")
    public TopicsResponse selectTopicList(@RequestParam Long grpId, @RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, HttpServletRequest request) {
        Long userId = SessionUtil.getUserId(request);
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userId);

        return new TopicsResponse(topicService.selectTopicList(userId, grpId, searchWord, order, direction));
    }

    @GetMapping("/{topicId}")
    public TopicResponse selectTopic(@PathVariable Long topicId, UserInfo userInfo) {
        // 토픽의 읽기 권한 체크
        authService.checkUserHasReadRoleAboutTopic(topicId, userInfo.getId());

        topicService.checkUserHasTopicReadRole(topicId, userInfo.getId());
        return new TopicResponse(topicService.selectTopic(topicId));
    }


    @PutMapping("/{topicId}")
    public TopicResponse updateTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(topicRequest.getId(), userInfo.getId());

        Topic topic = topicService.updateTopic(new Topic(topicRequest));
        pubTopic(new SimpleTopic(topic, StatusCode.UPDATE));
        return TopicResponse.builder().build();
    }

    @DeleteMapping("/{topicId}")
    public TopicResponse deleteTopic(@PathVariable Long topicId, UserInfo userInfo) {
        // 토픽의 쓰기 권한 체크
        authService.checkUserHasWriteRoleAboutTopic(topicId, userInfo.getId());

        topicService.deleteTopic(topicId);
        return TopicResponse.builder().build();
    }

}
