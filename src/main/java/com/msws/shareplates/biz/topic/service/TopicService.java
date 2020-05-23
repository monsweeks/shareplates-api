package com.msws.shareplates.biz.topic.service;

import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.entity.TopicUser;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
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

    public void checkUserHasTopicReadRole(Long topicId, Long userId) {
        Topic topic = selectTopic(topicId);
        boolean isIncludeUser = topic.getTopicUsers().stream().filter(topicUser -> topicUser.getUser().getId().equals(userId)).count() > 0;
        if (isIncludeUser) {
            return;
        }

        if (topic.getGrp().getPublicYn()) {
            return;
        }

        boolean isGrpUser = topic.getGrp().getUsers().stream().filter(grpUser -> grpUser.getUser().getId().equals(userId)).count() > 0;
        if (!topic.getPrivateYn() && isGrpUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }


    public List<Topic> selectTopicList(Long userId, Long grpId, String searchWord, String order, String direction) {
        return topicRepository.findTopicList(userId, grpId, searchWord, direction.equals("asc") ? Sort.by(order).ascending() : Sort.by(order).descending());
    }

    public Topic createTopic(Topic topic) {
        if (selectIsTopicNameExist(topic.getGrpId(), topic.getName())) {
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
        topic.setGrpId(topicInfo.getGrpId());
        topic.setPrivateYn(topicInfo.getPrivateYn());
        topic.setContent(topicInfo.getContent());

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

    public void updateTopicContent(long id, String content) {
        topicRepository.updateTopicContent(id, content);
    }

    public Topic selectTopic(long id) {
        return topicRepository.findByIdAndUseYnTrue(id).orElse(null);
    }

    public Boolean selectIsTopicNameExist(long grpId, String name) {
        return topicRepository.countByGrpIdAndName(grpId, name) > 0;
    }

    public void deleteTopic(long id) {
        Topic topic = topicRepository.findById(id).orElse(null);
        topic.setUseYn(false);
        topicRepository.save(topic);
    }

    public AuthCode selectUserTopicRole(Long topicId, Long userId) {

        Boolean isPrivateTopic = topicRepository.isPrivateTopic(topicId);
        Long topicMemberCount = topicRepository.countByTopicUserCount(topicId, userId);

        // 비밀 토픽이면, 토픽 멤버만 쓰기/보기 가능
        if (isPrivateTopic) {
            if (topicMemberCount > 0L) {
                return AuthCode.WRITE;
            }
            return AuthCode.NONE;
        } else {
            // 오픈된 토픽이고, 멤버면 쓰기 가능
            if (topicMemberCount > 0L) {
                return AuthCode.WRITE;
            }

            Boolean isPublicGrp = topicRepository.isPublicGrp(topicId);

            // 오픈된 토픽이고, 토픽의 ORG가 퍼블릭이 아니면, ORG의 권한에 따라 쓰기/읽기 가능
            String role = topicRepository.findUserGrpRole(topicId, userId);
            if (RoleCode.ADMIN.getCode().equals(role)) {
                return AuthCode.WRITE;
            } else if (RoleCode.MEMBER.getCode().equals(role)) {
                return AuthCode.READ;
            }

            // 오픈된 토픽이고, 토픽의 ORG가 퍼블릭이면 보기 가능
            if (isPublicGrp) {
                return AuthCode.READ;
            }
            return AuthCode.NONE;
        }
    }

}
