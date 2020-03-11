package com.giant.mindplates.biz.organization.service;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.entity.OrganizationUser;
import com.giant.mindplates.biz.organization.repository.OrganizationRepository;
import com.giant.mindplates.biz.organization.vo.OrganizationRole;
import com.giant.mindplates.biz.organization.vo.OrganizationStats;
import com.giant.mindplates.biz.organization.vo.request.CreateOrganizationRequest;
import com.giant.mindplates.biz.organization.vo.request.UpdateOrganizationRequest;
import com.giant.mindplates.biz.topic.repository.TopicRepository;
import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TopicRepository topicRepository;

    private void checkHasRole(Organization organization, UserInfo userInfo, Boolean checkAdmin) {

        if (checkAdmin && organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole().equals("ADMIN") && organizationUser.getUser().getId().equals(userInfo.getId())).count() < 1) {
            throw new ServiceException(ServiceExceptionCode.NO_ADMIN_USER);
        }

        if (!checkAdmin && organization.getUsers().stream().filter(organizationUser -> organizationUser.getUser().getId().equals(userInfo.getId())).count() < 1) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

    }

    public Organization selectOrganization(long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public OrganizationRole selectOrganizationRole(long id, UserInfo userInfo) {
        Organization organization = organizationRepository.findById(id).orElse(null);

        if (organization == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        this.checkHasRole(organization, userInfo, false);

        return new OrganizationRole(organization);
    }

    public List<Organization> selectUserOrganizationList(Long userId, Boolean includePublic) {
        List<Organization> organizations = new ArrayList<>();
        if (includePublic) {
            organizations.addAll(organizationRepository.findPublicOrganization());
        }
        organizations.addAll(organizationRepository.findUserOrganization(true, userId));
        return organizations;
    }

    public List<OrganizationStats> selectUserOrganizationStatList(Long userId) {
        return organizationRepository.findUserOrganizationStat(true, userId);
    }

    public List<Organization> selectPublicOrganizationList() {
        return organizationRepository.findPublicOrganization();
    }

    public Organization createOrganization(CreateOrganizationRequest createOrganizationRequest) {

        Organization organization = Organization.builder().name(createOrganizationRequest.getName()).description(createOrganizationRequest.getDescription()).publicYn(false).useYn(true).build();

        List<OrganizationUser> admins = createOrganizationRequest.getAdmins().stream().map(user -> OrganizationUser.builder().organization(organization).user(User.builder().id(user.getId()).build()).role("ADMIN").build()).collect(Collectors.toList());
        List<OrganizationUser> members = createOrganizationRequest.getMembers().stream().map(user -> OrganizationUser.builder().organization(organization).user(User.builder().id(user.getId()).build()).role("MEMBER").build()).collect(Collectors.toList());
        admins.addAll(members);

        organization.setUsers(admins);

        return organizationRepository.save(organization);
    }

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public void updateOrganization(UpdateOrganizationRequest updateOrganizationRequest, UserInfo userInfo) {

        Organization organization = organizationRepository.findById(updateOrganizationRequest.getId()).orElse(null);

        if (organization == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        this.checkHasRole(organization, userInfo, true);

        HashMap<Long, String> nextUserMap = new HashMap<>();
        for (UpdateOrganizationRequest.User admin : updateOrganizationRequest.getAdmins()) {
            nextUserMap.put(admin.getId(), "ADMIN");
        }
        for (UpdateOrganizationRequest.User admin : updateOrganizationRequest.getMembers()) {
            nextUserMap.put(admin.getId(), "MEMBER");
        }

        organization.setName(updateOrganizationRequest.getName());
        organization.setDescription(updateOrganizationRequest.getDescription());

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

        if (users.size() < 1 || users.stream().filter(user -> user.getRole().equals("ADMIN")).count() < 1) {
            throw new ServiceException(ServiceExceptionCode.NO_MANAGER_ASSIGNED);
        }

        organizationRepository.save(organization);
    }

    public void deleteOrganization(Long id, UserInfo userInfo) {
        Organization organization = selectOrganization(id);

        this.checkHasRole(organization, userInfo, true);

        Long count = topicRepository.countByOrganizationId(id);
        if (count > 0) {
            throw new ServiceException(ServiceExceptionCode.NO_MANAGER_ASSIGNED);
        }

        organization.setUseYn(false);
    }

    public Long selectPublicOrganizationCount() {
        return organizationRepository.countByUseYnTrueAndPublicYnTrue();
    }


}
