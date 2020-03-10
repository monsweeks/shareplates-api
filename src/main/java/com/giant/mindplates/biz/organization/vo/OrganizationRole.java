package com.giant.mindplates.biz.organization.vo;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRole extends RepresentationModel<OrganizationRole> {

    public OrganizationRole (Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
        this.publicYn = organization.getPublicYn();
        this.creationDate = organization.getCreationDate();
        this.lastUpdateDate = organization.getLastUpdateDate();
        this.admins = organization.getUsers().stream().filter(orgUser -> orgUser.getRole().equals("ADMIN")).map(orgUser -> orgUser.getUser()).collect(Collectors.toList());
        this.members = organization.getUsers().stream().filter(orgUser -> orgUser.getRole().equals("MEMBER")).map(orgUser -> orgUser.getUser()).collect(Collectors.toList());
    }

    List<User> members = new ArrayList<>();
    List<User> admins = new ArrayList<>();
    private Long id;
    private String name;
    private String description;
    private Boolean publicYn;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
}
