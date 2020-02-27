package com.giant.mindplates.biz.topic.service;

import com.giant.mindplates.biz.topic.entity.Topic;
import com.giant.mindplates.biz.topic.repository.TopicRepository;
import com.giant.mindplates.framework.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    public List<Topic> selectTopicList() {
        return topicRepository.findAll();
    }
    
    public Topic createTopic(Topic topic, Long userId)  {
        LocalDateTime now = LocalDateTime.now();
        
        topic.setUseYn(true);
        topic.setCreationDate(now);
        topic.setLastUpdateDate(now);
        topic.setCreatedBy(userId);
        topic.setLastUpdatedBy(userId);

        return topicRepository.save(topic);
    }

    public Topic selectTopic(long id) {
        return topicRepository.findById(id).orElse(null);
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
