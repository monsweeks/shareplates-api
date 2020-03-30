package com.msws.shareplates.biz.organization.vo.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.msws.shareplates.biz.organization.entity.Organization;
import com.msws.shareplates.common.code.AuthCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationResponse extends RepresentationModel<OrganizationResponse> {

    private Long id;
    private String name;
    private String description;
    private Boolean publicYn;
    private List<User> admins;
    private List<User> members;
    private Long userCount;
    private Long topicCount;
    private AuthCode role;
    private LocalDateTime creationDate;


    public OrganizationResponse(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
        this.publicYn = organization.getPublicYn();
        this.userCount = organization.getUserCount();
        this.topicCount = organization.getTopicCount();
        this.role = organization.getRole();
        this.creationDate = organization.getCreationDate();

        this.admins = organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole().equals("ADMIN")).map(organizationUser
                -> User.builder()
                .id(organizationUser.getUser().getId())
                .email(organizationUser.getUser().getEmail())
                .name(organizationUser.getUser().getName())
                .info(organizationUser.getUser().getInfo())
                .build()).collect(Collectors.toList());

        this.members = organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole().equals("MEMBER")).map(organizationUser
                -> User.builder()
                .id(organizationUser.getUser().getId())
                .email(organizationUser.getUser().getEmail())
                .name(organizationUser.getUser().getName())
                .info(organizationUser.getUser().getInfo())
                .build()).collect(Collectors.toList());
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
