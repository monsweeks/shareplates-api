package com.giant.mindplates.biz.organization.controller;

import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.biz.organization.vo.OrganizationRole;
import com.giant.mindplates.biz.organization.vo.OrganizationStats;
import com.giant.mindplates.biz.organization.vo.request.CreateOrganizationRequest;
import com.giant.mindplates.biz.organization.vo.request.UpdateOrganizationRequest;
import com.giant.mindplates.biz.organization.vo.response.CreateOrganizationResponse;
import com.giant.mindplates.common.util.SessionUtil;
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

    /**
     * 사용자가 포함된 ORG의 목록 조회
     *
     * @param userInfo
     * @return
     */
    @GetMapping("")
    public List<OrganizationStats> selectUserOrganizationStatList(@RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, UserInfo userInfo) {
        return organizationService.selectUserOrganizationStatList(userInfo.getId(), searchWord, order, direction);
    }

    /**
     * ORG 정보 조회
     *
     * @param organizationId
     * @param userInfo
     * @return
     */
    @GetMapping("/{organizationId}")
    public OrganizationRole getOrganization(@PathVariable Long organizationId, UserInfo userInfo) {
        return organizationService.selectOrganizationRole(organizationId, userInfo);
    }

    /**
     * ORG 생성
     *
     * @param createOrganizationRequest
     * @return
     */
    @PostMapping("")
    public CreateOrganizationResponse create(@RequestBody CreateOrganizationRequest createOrganizationRequest) {
        organizationService.createOrganization(createOrganizationRequest);
        Link link = new Link("/organizations", "organizations");

        return CreateOrganizationResponse.builder()
                .build()
                .add(link);
    }

    /**
     * ORG 수정
     *
     * @param updateOrganizationRequest
     * @return
     */
    @PutMapping("/{organizationId}")
    public CreateOrganizationResponse update(@RequestBody UpdateOrganizationRequest updateOrganizationRequest, UserInfo userInfo) {
        organizationService.updateOrganization(updateOrganizationRequest, userInfo);

        Link link = new Link("/organizations", "organizations");

        return CreateOrganizationResponse.builder()
                .build()
                .add(link);
    }


    /**
     * ORG 삭제
     * @param organizationId
     * @param userInfo
     * @return
     */
    @DeleteMapping("/{organizationId}")
    public CreateOrganizationResponse update(@PathVariable Long organizationId, UserInfo userInfo) {
        organizationService.deleteOrganization(organizationId, userInfo);

        Link link = new Link("/organizations", "organizations");

        return CreateOrganizationResponse.builder()
                .build()
                .add(link);
    }


}
