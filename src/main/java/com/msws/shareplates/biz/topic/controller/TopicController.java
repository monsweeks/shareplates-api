package com.msws.shareplates.biz.topic.controller;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.common.service.AuthService;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.AccessCodeService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.request.ShareRequest;
import com.msws.shareplates.biz.share.vo.response.AccessCodeResponse;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.share.vo.response.ShareResponse;
import com.msws.shareplates.biz.share.vo.response.SharesResponse;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.request.TopicRequest;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.aop.annotation.CheckShareAuth;
import com.msws.shareplates.framework.aop.annotation.CheckTopicAuth;
import com.msws.shareplates.framework.aop.annotation.WriteAuth;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.stream.Collectors;

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

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private AccessCodeService accessCodeService;


    @GetMapping("/{topicId}")
    @CheckTopicAuth
    public TopicResponse selectTopic(@PathVariable Long topicId, UserInfo userInfo) {
        return new TopicResponse(topicService.selectTopic(topicId));
    }

    @ApiOperation(value = "토픽 정보 조회 (공유 생성을 위한 기초 정보")
    @GetMapping("/{topicId}/shares/prepare")
    @CheckTopicAuth
    public ShareInfo createAccessCodeAndSelectTopic(@PathVariable Long topicId, UserInfo userInfo) throws NoSuchProviderException, NoSuchAlgorithmException {
        return ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(topicId)))
                .chapters(chapterService.selectChapters(topicId).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .accessCode(new AccessCodeResponse(accessCodeService.createAccessCode(userInfo.getId())))
                .build();
    }

    @ApiOperation(value = "공유 정보 조회 (편집)")
    @GetMapping("/{topicId}/shares/{shareId}/info")
    @CheckTopicAuth
    @WriteAuth
    public ShareInfo selectShare(@PathVariable Long topicId, @PathVariable Long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);
        return ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(share.getTopic().getId())))
                .chapters(chapterService.selectChapters(share.getTopic().getId()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .accessCode(new AccessCodeResponse(accessCodeService.selectAccessCodeByCode(share.getAccessCode())))
                .share(new ShareResponse(share))
                .build();
    }

    @PutMapping("/{topicId}")
    @CheckTopicAuth
    public TopicResponse updateTopic(@PathVariable Long topicId, @RequestBody TopicRequest topicRequest, UserInfo userInfo) {
        Topic topic = topicService.updateTopic(new Topic(topicRequest));
        return new TopicResponse(topic);
    }

    @ApiOperation(value = "토픽 속성 수정")
    @PutMapping("/{topicId}/content")
    @CheckTopicAuth
    public EmptyResponse updateTopicContent(@PathVariable Long topicId, @RequestBody TopicRequest topicRequest, UserInfo userInfo) {
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
    public SharesResponse selectTopicShareList(@PathVariable Long topicId, UserInfo userInfo) {
        return new SharesResponse(shareService.selectShareListByTopicId(topicId));
    }

    @ApiOperation(value = "공유 정보 생성")
    @PostMapping("/{topicId}/shares")
    @CheckTopicAuth
    public ShareResponse createShare(@PathVariable Long topicId, @RequestBody ShareRequest shareRequest, UserInfo userInfo) {
        Share share = shareService.createShare(new Share(shareRequest), userInfo.getId());
        return new ShareResponse(share);
    }

}
