package com.msws.shareplates.common.bean;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import com.msws.shareplates.biz.organization.entity.Organization;
import com.msws.shareplates.biz.organization.service.OrganizationService;

import lombok.extern.java.Log;

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
