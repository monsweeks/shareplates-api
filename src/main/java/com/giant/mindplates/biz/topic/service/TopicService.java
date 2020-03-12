package com.giant.mindplates.biz.topic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giant.mindplates.biz.topic.entity.Topic;
import com.giant.mindplates.biz.topic.entity.TopicUser;
import com.giant.mindplates.biz.topic.entity.TopicUserId;
import com.giant.mindplates.biz.topic.repository.TopicRepository;
import com.giant.mindplates.biz.topic.repository.TopicUserRepository;
import com.giant.mindplates.biz.topic.vo.request.CreateTopicReqeust;
import com.giant.mindplates.biz.topic.vo.request.UpdateTopicRequest;
import com.giant.mindplates.biz.topic.vo.response.GetTopicsResponse;
import com.giant.mindplates.common.code.StatusCode;
import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.framework.exception.BizException;

@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private TopicUserRepository topicUserRepository;

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    public GetTopicsResponse selectTopicList() {
        return GetTopicsResponse.builder()
                .topics(topicRepository.findAllByUseYnTrue().stream().map(topic
                        -> GetTopicsResponse.Topic.builder()
                        .id(topic.getId())
                        .summary(topic.getSummary())
                        .name(topic.getName())
                        .iconIndex(topic.getIconIndex())
                        .privateYn(topic.getPrivateYn())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public com.giant.mindplates.biz.topic.vo.Topic createTopic(CreateTopicReqeust createTopicRequest) {

        if (checkName(createTopicRequest.getOrganizationId(), createTopicRequest.getName()))
            throw new ServiceException(ServiceExceptionCode.TOPIC_ALREADY_EXISTS);

        Topic topic = Topic.builder()
                .name(createTopicRequest.getName())
                .summary(createTopicRequest.getSummary())
                .iconIndex(createTopicRequest.getIconIndex())
                .organizationId(createTopicRequest.getOrganizationId())
                .privateYn(createTopicRequest.isPrivateYn())
                .useYn(true)
                .build();

        List<TopicUser> topicUsers = createTopicRequest.getUsers().stream().map(user
                -> TopicUser.builder()
                .topic(topic)
                .topicUserId(TopicUserId.builder()
                        .userId(user.getId())
                        .build())
                .build())
                .collect(Collectors.toList());

        topic.setTopicUser(topicUsers);

        topicRepository.save(topic);
        
        return com.giant.mindplates.biz.topic.vo.Topic.builder()
                .name(topic.getName())
                .id(topic.getId())
                .summary(topic.getSummary())
                .iconIndex(topic.getIconIndex())
                .privateYn(topic.getPrivateYn())
                .statusCode(StatusCode.CREATE)
        		.build();
    }
    
    public void deleteTopicUser(UpdateTopicRequest updateTopicRequest) {
    	topicUserRepository.deleteByTopicId(updateTopicRequest.getId());
    }

    public com.giant.mindplates.biz.topic.vo.Topic updateTopic(UpdateTopicRequest updateTopicRequest) {   	
    	
        Topic topic = Topic.builder()
        		.id(updateTopicRequest.getId())
        		.name(updateTopicRequest.getName())
                .summary(updateTopicRequest.getSummary())
                .iconIndex(updateTopicRequest.getIconIndex())
                .organizationId(updateTopicRequest.getOrganizationId())
                .privateYn(updateTopicRequest.isPrivateYn())
                .useYn(true)
                .build();

        List<TopicUser> topicUsers = updateTopicRequest.getUsers().stream().map(user
                -> TopicUser.builder()
                .topic(topic)
                .topicUserId(TopicUserId.builder()
                        .userId(user.getId())
                        .topicId(updateTopicRequest.getId())
                        .build())
                .build())
                .collect(Collectors.toList());

        topic.setTopicUser(topicUsers);

        topicRepository.save(topic);
        
        return com.giant.mindplates.biz.topic.vo.Topic.builder()
                .id(topic.getId())
                .name(topic.getName())
                .summary(topic.getSummary())
                .iconIndex(topic.getIconIndex())
                .privateYn(topic.getPrivateYn())
                .statusCode(StatusCode.UPDATE)
        		.build();
    }

    public Topic selectTopic(long id) {
        return topicRepository.findByIdAndUseYnTrue(id).orElse(null);
    }

    public Boolean checkName(long organizationId, String name) {
        return topicRepository.countByOrganizationIdAndName(organizationId, name) > 0;
    }

    public void deleteTopic(long id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if (topic == null) {
            throw new BizException(messageSourceAccessor.getMessage("error.resourceNotFound"));
        } else {
            topic.setUseYn(false);
            topicRepository.save(topic);
        }

    }

}
