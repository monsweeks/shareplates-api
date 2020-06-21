package com.msws.shareplates.biz.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.response.SharesResponse;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.request.TopicRequest;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.topic.vo.response.TopicsResponse;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.aop.annotation.CheckTopicAuth;
import com.msws.shareplates.framework.session.vo.UserInfo;

import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @Autowired
    ShareService shareService;

    @Autowired
    AuthService authService;

    @PostMapping("")
    public TopicResponse createTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutGrp(topicRequest.getGrpId(), userInfo.getId());
        Topic topic = topicService.createTopic(new Topic(topicRequest));
        return new TopicResponse(topic);
    }

    @GetMapping("/exist")
    public Boolean selectTopicNameExist(@RequestParam Long grpId, @RequestParam String name, @RequestParam(required = false) Long topicId, UserInfo userInfo) {
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userInfo.getId());
        return topicService.selectIsTopicNameExist(grpId, name, topicId);
    }

    @DisableLogin
    @GetMapping("")
    public TopicsResponse selectTopicList(@RequestParam Long grpId, @RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, UserInfo info) {
        Long userId = info != null ? info.getId() : null;
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userId);
        return new TopicsResponse(topicService.selectTopicList(userId, grpId, searchWord, order, direction));
    }

    @GetMapping("/{topicId}")
    @CheckTopicAuth
    public TopicResponse selectTopic(@PathVariable Long topicId, UserInfo userInfo) {
        return new TopicResponse(topicService.selectTopic(topicId));
    }

    @PutMapping("/{topicId}")
    @CheckTopicAuth
    public TopicResponse updateTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        Topic topic = topicService.updateTopic(new Topic(topicRequest));
        return new TopicResponse(topic);
    }

    @ApiOperation(value = "토픽 속성 수정")
    @PutMapping("/{topicId}/content")
    @CheckTopicAuth
    public EmptyResponse updateTopicContent(@PathVariable Long topicId, @RequestBody TopicRequest topicRequest , UserInfo userInfo) {
        topicService.updateTopicContent(topicId, topicRequest.getContent());
        return EmptyResponse.getInstance();
    }

    @DeleteMapping("/{topicId}")
    @CheckTopicAuth
    public TopicResponse deleteTopic(@PathVariable Long topicId, UserInfo userInfo) {
        topicService.deleteTopic(topicId);
        return TopicResponse.builder().build();
    }

    @GetMapping("/{topicId}/shares")
    @CheckTopicAuth
    public SharesResponse selectTopicShareList(@PathVariable Long topicId) {
        return new SharesResponse(shareService.selectShareListByTopicId(topicId));
    }

}
