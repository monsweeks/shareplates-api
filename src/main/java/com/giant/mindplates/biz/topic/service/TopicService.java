package com.giant.mindplates.biz.topic.service;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.biz.topic.entity.Topic;
import com.giant.mindplates.biz.topic.entity.TopicUser;
import com.giant.mindplates.biz.topic.repository.TopicRepository;
import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private OrganizationService organizationService;

    public void checkUserHasTopicReadRole(Long topicId, Long userId) {
        Topic topic = selectTopic(topicId);
        boolean isIncludeUser = topic.getTopicUsers().stream().filter(topicUser -> topicUser.getUser().getId().equals(userId)).count() > 0;
        if (isIncludeUser) {
            return;
        }

        if (topic.getOrganization().getPublicYn()) {
            return;
        }

        boolean isOrgUser = topic.getOrganization().getUsers().stream().filter(organizationUser -> organizationUser.getUser().getId().equals(userId)).count() > 0;
        if (!topic.getPrivateYn() && isOrgUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }

    public void checkUserHasTopicWriteRole(Long topicId, Long userId) {
        Topic topic = selectTopic(topicId);
        if (topic == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        boolean isIncludeUser = topic.getTopicUsers().stream().filter(topicUser -> topicUser.getUser().getId().equals(userId)).count() > 0;
        if (isIncludeUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }

    public void checkOrgIncludesUser(Long organizationId, Long userId) {
        Organization organization = organizationService.selectOrganization(organizationId);

        if (organization == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        if (organization.getPublicYn()) {
            return;
        }

        boolean isIncludeUser = organization.getUsers().stream().filter(organizationUser -> organizationUser.getUser().getId().equals(userId)).count() > 0;
        if (isIncludeUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }

    public List<Topic> selectTopicList(Long userId, Long organizationId, String searchWord, String order, String direction) {
        return topicRepository.findTopicList(userId, organizationId, searchWord, direction.equals("asc") ? Sort.by(order).ascending() : Sort.by(order).descending());
    }

    public Topic createTopic(Topic topic) {
        if (selectIsTopicNameExist(topic.getOrganizationId(), topic.getName())) {
            throw new ServiceException(ServiceExceptionCode.TOPIC_ALREADY_EXISTS);
        }

        if (topic.getTopicUsers().size() < 1) {
            throw new ServiceException(ServiceExceptionCode.TOPIC_NO_USER_ASSIGNED);
        }

        return topicRepository.save(topic);
    }


    public Topic updateTopic(Topic topicInfo) {

        Topic topic = topicRepository.findById(topicInfo.getId()).orElse(null);

        HashMap<Long, Boolean> nextUserMap = new HashMap<>();
        for (TopicUser topicUser : topicInfo.getTopicUsers()) {
            nextUserMap.put(topicUser.getUser().getId(), true);
        }

        topic.setName(topicInfo.getName());
        topic.setSummary(topicInfo.getSummary());
        topic.setOrganizationId(topicInfo.getOrganizationId());
        topic.setIconIndex(topicInfo.getIconIndex());
        topic.setPrivateYn(topicInfo.getPrivateYn());

        // REMOVE
        HashMap<Long, Boolean> currentUserMap = new HashMap<>();
        List<TopicUser> topicUsers = topic.getTopicUsers();
        Iterator iterator = topicUsers.iterator();
        while (iterator.hasNext()) {
            TopicUser topicUser = (TopicUser) iterator.next();
            if (nextUserMap.containsKey(topicUser.getUser().getId())) {
                currentUserMap.put(topicUser.getUser().getId(), true);
            } else {
                iterator.remove();
            }
        }

        // INSERT
        for (Long userId : nextUserMap.keySet()) {
            if (!currentUserMap.containsKey(userId)) {
                topicUsers.add(TopicUser.builder().user(User.builder().id(userId).build()).topic(topic).build());
            }
        }

        if (topicUsers.size() < 1) {
            throw new ServiceException(ServiceExceptionCode.TOPIC_NO_USER_ASSIGNED);
        }

        return topicRepository.save(topic);
    }

    public Topic selectTopic(long id) {
        return topicRepository.findByIdAndUseYnTrue(id).orElse(null);
    }

    public Boolean selectIsTopicNameExist(long organizationId, String name) {
        return topicRepository.countByOrganizationIdAndName(organizationId, name) > 0;
    }

    public void deleteTopic(long id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        topic.setUseYn(false);
        topicRepository.save(topic);
    }

}
