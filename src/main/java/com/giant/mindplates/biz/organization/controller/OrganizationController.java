package com.giant.mindplates.biz.organization.controller;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @PostMapping("")
    public Organization sampleMvcRestController(String name) {
        Organization organization = Organization.builder().name(name).deleteYn(false).useYn(true).build();
        organizationService.save(organization);
        return organization;
    }

}
