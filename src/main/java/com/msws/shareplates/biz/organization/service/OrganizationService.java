package com.msws.shareplates.biz.organization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msws.shareplates.biz.organization.entity.Organization;
import com.msws.shareplates.biz.organization.entity.OrganizationUser;
import com.msws.shareplates.biz.organization.repository.OrganizationRepository;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TopicRepository topicRepository;

    public void checkOrgIncludesUser(Long organizationId, Long userId) {
        Organization organization = selectOrganization(organizationId);

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

    public void checkIsUserOrgAdmin(Long organizationId, Long userId) {
        Organization organization = selectOrganization(organizationId);

        if (organization == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        if (organization.getPublicYn()) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        boolean isAdminUser = organization.getUsers().stream().filter(organizationUser -> organizationUser.getUser().getId().equals(userId) && organizationUser.getRole() == AuthCode.ADMIN).count() > 0;
        if (isAdminUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }

    public Organization selectOrganization(long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public List<Organization> selectUserOrganizationList(Long userId, Boolean includePublic) {
        List<Organization> organizations = new ArrayList<>();
        if (includePublic) {
            organizations.addAll(organizationRepository.findPublicOrganization());
        }
        organizations.addAll(organizationRepository.findUserOrganization(true, userId));
        return organizations;
    }

    public List<Organization> selectOrganizationListByUser(Long userId, String searchWord, String order, String direction) {
        return organizationRepository.findOrganizationListByUser(userId, searchWord, true, direction.equals("asc") ? Sort.by(order).ascending() : Sort.by(order).descending());
    }

    public List<Organization> selectPublicOrganizationList() {
        return organizationRepository.findPublicOrganization();
    }

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public void updateOrganization(Organization organizationInfo) {

        Organization organization = organizationRepository.findById(organizationInfo.getId()).orElse(null);

        HashMap<Long, AuthCode> nextUserMap = new HashMap<>();
        for (OrganizationUser user : organizationInfo.getUsers()) {
            nextUserMap.put(user.getUser().getId(), user.getRole());
        }

        organization.setName(organizationInfo.getName());
        organization.setDescription(organizationInfo.getDescription());

        // UPDATE AND REMOVE
        HashMap<Long, Boolean> currentUserMap = new HashMap<>();
        List<OrganizationUser> users = organization.getUsers();
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            OrganizationUser user = (OrganizationUser) iterator.next();
            if (nextUserMap.containsKey(user.getUser().getId())) {
                user.setRole(nextUserMap.get(user.getUser().getId()));
                currentUserMap.put(user.getUser().getId(), true);
            } else {
                iterator.remove();
            }
        }

        // INSERT
        for (Long userId : nextUserMap.keySet()) {
            if (!currentUserMap.containsKey(userId)) {
                users.add(OrganizationUser.builder().user(User.builder().id(userId).build()).organization(organization).role(nextUserMap.get(userId)).build());
            }
        }

        if (users.size() < 1 || users.stream().filter(user -> user.getRole() == AuthCode.ADMIN).count() < 1) {
            throw new ServiceException(ServiceExceptionCode.NO_MANAGER_ASSIGNED);
        }

        organizationRepository.save(organization);
    }

    public void deleteOrganization(Long organizationId) {
        Organization organization = selectOrganization(organizationId);
        Long count = topicRepository.countByOrganizationId(organizationId);
        if (count > 0) {
            throw new ServiceException(ServiceExceptionCode.NO_EMPTY_ORGANIZATION);
        }
        organization.setUseYn(false);
        organizationRepository.save(organization);
    }

    public Long selectPublicOrganizationCount() {
        return organizationRepository.countByUseYnTrueAndPublicYnTrue();
    }


}
