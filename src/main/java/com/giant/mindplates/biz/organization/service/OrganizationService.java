package com.giant.mindplates.biz.organization.service;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> listAll() {
        return organizationRepository.findAll();
    }

    public void save(Organization organization) {
        organizationRepository.save(organization);
    }

    public Organization get(long id) {
        return organizationRepository.findById(id).get();
    }

    public void delete(long id) {
        organizationRepository.deleteById(id);
    }

}
