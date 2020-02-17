package com.giant.mindplates.biz.organization.controller;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @PostMapping("")
    public Organization create(String name) {
        Organization organization = Organization.builder().name(name).deleteYn(false).useYn(true).build();
        organizationService.save(organization);
        return organization;
    }

    @GetMapping("")
    public List<Organization> list() {
        return organizationService.listAll();
    }

}
