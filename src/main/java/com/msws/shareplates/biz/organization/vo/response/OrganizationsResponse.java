package com.msws.shareplates.biz.organization.vo.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.msws.shareplates.common.code.AuthCode;

import lombok.Builder;
import lombok.Data;

@Data
public class OrganizationsResponse extends RepresentationModel<OrganizationsResponse> {

    private List<Organization> organizations;

    public OrganizationsResponse(List<com.msws.shareplates.biz.organization.entity.Organization> organizations) {
        this.organizations = organizations.stream().map(organization
                -> Organization.builder()
                .id(organization.getId())
                .name(organization.getName())
                .description(organization.getDescription())
                .publicYn(organization.getPublicYn())
                .userCount(organization.getUserCount())
                .topicCount(organization.getTopicCount())
                .role(organization.getRole())
                .admins(organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole() == AuthCode.ADMIN).map(organizationUser
                        -> User.builder()
                        .id(organizationUser.getUser().getId())
                        .email(organizationUser.getUser().getName())
                        .name(organizationUser.getUser().getName())
                        .info(organizationUser.getUser().getInfo())
                        .build()).collect(Collectors.toList()))
                .members(organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole()== AuthCode.MEMBER).map(organizationUser
                        -> User.builder()
                        .id(organizationUser.getUser().getId())
                        .email(organizationUser.getUser().getName())
                        .name(organizationUser.getUser().getName())
                        .info(organizationUser.getUser().getInfo())
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

    }

    @Builder
    @Data
    public static class Organization extends RepresentationModel<Organization> {
        private Long id;
        private String name;
        private String description;
        private Boolean publicYn;
        private Long userCount;
        private Long topicCount;
        private AuthCode role;
        private List<User> admins;
        private List<User> members;
    }

    @Builder
    @Data
    public static class User {
        private Long id;
        private String email;
        private String name;
        private String info;
    }
}
