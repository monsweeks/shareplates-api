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
import com.giant.mindplates.biz.topic.vo.request.CreateTopicReqeust;
import com.giant.mindplates.biz.topic.vo.response.GetTopicsResponse;
import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.framework.exception.BizException;

@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    public GetTopicsResponse selectTopicList() {
        return GetTopicsResponse.builder()
        		.topics(topicRepository.findAll().stream().map(topic 
        				-> GetTopicsResponse.Topic.builder()
	        				.id(topic.getId())
	        				.summary(topic.getSummary())
	        				.name(topic.getName())
        				.build()).collect(Collectors.toList()))
        		.build();
    }
    
    public void createTopic(CreateTopicReqeust createTopicRequest)  {
        
    	if(checkName(createTopicRequest.getOrganizationId(), createTopicRequest.getName()))
    		throw new ServiceException(ServiceExceptionCode.TOPIC_ALREADY_EXISTS);
    	
    	Topic topic = Topic.builder()
						.name(createTopicRequest.getName())
						.summary(createTopicRequest.getSummary())
						.iconIndex(createTopicRequest.getIconIndex())
						.organizationId(createTopicRequest.getOrganizationId())
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
    }

    public Topic selectTopic(long id) {
        return topicRepository.findById(id).orElse(null);
    }

    public Boolean checkName(long organizationId, String name) {
        return topicRepository.countByOrganizationIdAndName(organizationId, name) > 0;
    }

    public void deleteTopic(long id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        if (topic != null) {
            throw new BizException(messageSourceAccessor.getMessage("error.resourceNotFound"));
        } else {
            topic.setUseYn(false);
            topicRepository.save(topic);
        }

    }

}
