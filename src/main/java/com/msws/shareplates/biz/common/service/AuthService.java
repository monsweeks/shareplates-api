package com.msws.shareplates.biz.common.service;

import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private GrpService grpService;

    @Autowired
    private TopicService topicService;

    public void checkUserHasWriteRoleAboutGrp(Long grpId, Long userId) {
        AuthCode auth = grpService.selectUserGrpRole(grpId, userId);
        if (auth != AuthCode.WRITE) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
    }

    public void checkUserHasReadRoleAboutGrp(Long grpId, Long userId) {
        AuthCode auth = grpService.selectUserGrpRole(grpId, userId);
        if (auth == AuthCode.NONE) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
    }

    public void checkUserHasReadRoleAboutTopic(Long topicId, Long userId) {
        AuthCode auth = topicService.selectUserTopicRole(topicId, userId);
        if (auth == AuthCode.NONE) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
    }

    public void checkUserHasWriteRoleAboutTopic(Long topicId, Long userId) {
        AuthCode auth = topicService.selectUserTopicRole(topicId, userId);
        if (auth != AuthCode.WRITE) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
    }


}
