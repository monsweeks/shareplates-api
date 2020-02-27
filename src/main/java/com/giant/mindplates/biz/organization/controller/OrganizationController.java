package com.giant.mindplates.biz.organization.controller;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @Autowired
    SessionUtil sessionUtil;

    @PostMapping("")
    public Organization create(Organization organization) {
        organizationService.createOrganization(organization);
        return organization;
    }

    @DisableLogin
    @GetMapping("/my")
    public List<Organization> selectUserOrganizationList(HttpServletRequest request) {
        Long userId = sessionUtil.getUserId(request);
        return organizationService.selectUserOrganizationList(userId);
    }

}
