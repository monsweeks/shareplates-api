package com.giant.mindplates.biz.organization.service;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.entity.OrganizationUser;
import com.giant.mindplates.biz.organization.repository.OrganizationRepository;
import com.giant.mindplates.biz.organization.vo.request.CreateOrganizationRequest;
import com.giant.mindplates.biz.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

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

    public List<Organization> selectPublicOrganizationList() {
        return organizationRepository.findPublicOrganization();
    }

    public List<Organization> selectOrganizationList() {
        return organizationRepository.findAll();
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

    public void updateOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    public void deleteOrganization(long id) {
        Organization organization = selectOrganization(id);
        organization.setUseYn(false);
    }

    public Long selectPublicOrganizationCount() {
        return organizationRepository.countByUseYnTrueAndPublicYnTrue();
    }


}
