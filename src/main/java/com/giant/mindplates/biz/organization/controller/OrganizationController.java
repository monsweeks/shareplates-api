package com.giant.mindplates.biz.organization.controller;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.biz.organization.vo.request.CreateOrganizationRequest;
import com.giant.mindplates.biz.organization.vo.response.CreateOrganizationResponse;
import com.giant.mindplates.common.util.SessionUtil;
import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @Autowired
    SessionUtil sessionUtil;

    @PostMapping("")
    public CreateOrganizationResponse create(@RequestBody CreateOrganizationRequest createOrganizationRequest) {
        organizationService.createOrganization(createOrganizationRequest);
        Link link = new Link("/organizations", "organizations");

        return CreateOrganizationResponse.builder()
                .build()
                .add(link);
    }

    @DisableLogin
    @GetMapping("/my")
    public List<Organization> selectUserAndPublicOrganizationList(UserInfo userInfo) {
        return organizationService.selectUserOrganizationList(userInfo.getId(), true);
    }

    @GetMapping("")
    public List<Organization> selectUserOrganizationList(UserInfo userInfo) {
        return organizationService.selectUserOrganizationList(userInfo.getId(), false);
    }

}
