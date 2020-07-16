package com.msws.shareplates.biz.grp.controller;

import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.grp.entity.Grp;
import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.grp.vo.request.GrpRequest;
import com.msws.shareplates.biz.grp.vo.response.GrpResponse;
import com.msws.shareplates.biz.grp.vo.response.GrpsResponse;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.request.TopicRequest;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.topic.vo.response.TopicsResponse;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GrpController {
    @Autowired
    GrpService grpService;

    @Autowired
    AuthService authService;

    @Autowired
    TopicService topicService;

    @GetMapping("")
    public GrpsResponse selectGrpListByUser(@RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, UserInfo userInfo) {
        return new GrpsResponse(grpService.selectGrpListByUser(userInfo.getId(), searchWord, order, direction));
    }

    @DisableLogin
    @GetMapping("/{grpId}/topics")
    public TopicsResponse selectTopicList(@PathVariable Long grpId, @RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, UserInfo info) {
        Long userId = info != null ? info.getId() : null;
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userId);
        return new TopicsResponse(topicService.selectTopicList(userId, grpId, searchWord, order, direction));
    }

    @PostMapping("/{grpId}/topics")
    public TopicResponse createTopic(@RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        // 토픽을 작성하려는 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutGrp(topicRequest.getGrpId(), userInfo.getId());
        Topic topic = topicService.createTopic(new Topic(topicRequest));
        return new TopicResponse(topic);
    }

    @GetMapping("/{grpId}/topics/exist")
    public Boolean selectTopicNameExist(@PathVariable Long grpId, @RequestParam String name, @RequestParam(required = false) Long topicId, UserInfo userInfo) {
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userInfo.getId());
        return topicService.selectIsTopicNameExist(grpId, name, topicId);
    }

    @GetMapping("/{grpId}")
    public GrpResponse selectGrp(@PathVariable Long grpId, UserInfo userInfo) {
        // 그룹의 읽기 권한 확인
        authService.checkUserHasReadRoleAboutGrp(grpId, userInfo.getId());

        grpService.checkGrpIncludesUser(grpId, userInfo.getId());
        return new GrpResponse(grpService.selectGrp(grpId));
    }

    @PostMapping("")
    public GrpResponse createGrp(@RequestBody GrpRequest grpRequest) {
        grpService.createGrp(new Grp(grpRequest));
        return GrpResponse.builder().build();
    }

    @PutMapping("/{grpId}")
    public GrpResponse updateGrp(@RequestBody GrpRequest grpRequest, UserInfo userInfo) {
        // 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutGrp(grpRequest.getId(), userInfo.getId());

        grpService.checkIsUserGrpAdmin(grpRequest.getId(), userInfo.getId());
        grpService.updateGrp(new Grp(grpRequest));
        return GrpResponse.builder().build();
    }

    @DeleteMapping("/{grpId}")
    public GrpResponse deleteGrp(@PathVariable Long grpId, UserInfo userInfo) {
        // 그룹의 쓰기 권한 확인
        authService.checkUserHasWriteRoleAboutGrp(grpId, userInfo.getId());

        grpService.checkIsUserGrpAdmin(grpId, userInfo.getId());
        grpService.deleteGrp(grpId);
        return GrpResponse.builder().build();
    }

}
