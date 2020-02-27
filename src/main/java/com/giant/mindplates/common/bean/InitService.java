package com.giant.mindplates.common.bean;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import lombok.extern.java.Log;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log
@Transactional
public class InitService {

    private OrganizationService organizationService;

    public InitService (OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    public void init() {
        Long count = organizationService.selectPublicOrganizationCount();
        if (count < 1) {
            LocalDateTime now = LocalDateTime.now();
            Organization publicOrganization = Organization.builder().name("default").useYn(true).publicYn(true).build();
            publicOrganization.setCreationDate(now);
            publicOrganization.setLastUpdateDate(now);
            organizationService.createOrganization(publicOrganization);
        }
    }
}
