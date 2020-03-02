package com.giant.mindplates.biz.organization.service;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization selectOrganization(long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    public List<Organization> selectUserOrganizationList(Long userId) {
        List<Organization> organizations = new ArrayList<>();
        organizations.addAll(organizationRepository.findPublicOrganization());
        organizations.addAll(organizationRepository.findUserOrganization(true, userId));
        return organizations;
    }

    public List<Organization> selectPublicOrganizationList() {
        return organizationRepository.findPublicOrganization();
    }

    public List<Organization> selectOrganizationList() {
        return organizationRepository.findAll();
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
