package com.msws.shareplates.biz.organization.controller;

import com.msws.shareplates.biz.organization.entity.Organization;
import com.msws.shareplates.biz.organization.service.OrganizationService;
import com.msws.shareplates.biz.organization.vo.request.OrganizationRequest;
import com.msws.shareplates.biz.organization.vo.response.OrganizationResponse;
import com.msws.shareplates.biz.organization.vo.response.OrganizationsResponse;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @Autowired
    SessionUtil sessionUtil;

    @GetMapping("")
    public OrganizationsResponse selectOrganizationListByUser(@RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, UserInfo userInfo) {
        return new OrganizationsResponse(organizationService.selectOrganizationListByUser(userInfo.getId(), searchWord, order, direction));
    }

    @GetMapping("/{organizationId}")
    public OrganizationResponse selectOrganization(@PathVariable Long organizationId, UserInfo userInfo) {
        organizationService.checkOrgIncludesUser(organizationId, userInfo.getId());
        return new OrganizationResponse(organizationService.selectOrganization(organizationId));
    }

    @PostMapping("")
    public OrganizationResponse createOrganization(@RequestBody OrganizationRequest organizationRequest) {
        organizationService.createOrganization(new Organization(organizationRequest));
        Link link = new Link("/organizations", "organizations");
        return OrganizationResponse.builder().build().add(link);
    }

    @PutMapping("/{organizationId}")
    public OrganizationResponse updateOrganization(@RequestBody OrganizationRequest OrganizationRequest, UserInfo userInfo) {
        organizationService.checkIsUserOrgAdmin(OrganizationRequest.getId(), userInfo.getId());
        organizationService.updateOrganization(new Organization(OrganizationRequest));
        Link link = new Link("/organizations", "organizations");
        return OrganizationResponse.builder().build().add(link);
    }

    @DeleteMapping("/{organizationId}")
    public OrganizationResponse deleteOrganization(@PathVariable Long organizationId, UserInfo userInfo) {
        organizationService.checkIsUserOrgAdmin(organizationId, userInfo.getId());
        organizationService.deleteOrganization(organizationId);
        Link link = new Link("/organizations", "organizations");
        return OrganizationResponse.builder().build().add(link);
    }

}
