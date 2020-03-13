package com.giant.mindplates.biz.organization.vo.response;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.entity.OrganizationUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

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
    private String role;

    public OrganizationResponse(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
        this.publicYn = organization.getPublicYn();
        this.userCount = organization.getUserCount();
        this.topicCount = organization.getTopicCount();
        this.role = organization.getRole();

        this.admins = organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole().equals("ADMIN")).map(organizationUser
                -> User.builder()
                .id(organizationUser.getUser().getId())
                .email(organizationUser.getUser().getName())
                .name(organizationUser.getUser().getName())
                .info(organizationUser.getUser().getInfo())
                .build()).collect(Collectors.toList());

        this.members = organization.getUsers().stream().filter(organizationUser -> organizationUser.getRole().equals("MEMBER")).map(organizationUser
                -> User.builder()
                .id(organizationUser.getUser().getId())
                .email(organizationUser.getUser().getName())
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
