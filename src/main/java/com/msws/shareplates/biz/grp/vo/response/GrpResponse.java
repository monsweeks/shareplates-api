package com.msws.shareplates.biz.grp.vo.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.msws.shareplates.biz.grp.entity.Grp;
import com.msws.shareplates.common.code.RoleCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GrpResponse extends RepresentationModel<GrpResponse> {

    private Long id;
    private String name;
    private String description;
    private Boolean publicYn;
    private List<User> admins;
    private List<User> members;
    private Long userCount;
    private Long topicCount;
    private RoleCode role;
    private LocalDateTime creationDate;


    public GrpResponse(Grp grp) {
        this.id = grp.getId();
        this.name = grp.getName();
        this.description = grp.getDescription();
        this.publicYn = grp.getPublicYn();
        this.userCount = grp.getUserCount();
        this.topicCount = grp.getTopicCount();
        this.role = grp.getRole();
        this.creationDate = grp.getCreationDate();

        this.admins = grp.getUsers().stream().filter(grpUser -> RoleCode.ADMIN.equals(grpUser.getRole())).map(grpUser
                -> User.builder()
                .id(grpUser.getUser().getId())
                .email(grpUser.getUser().getEmail())
                .name(grpUser.getUser().getName())
                .info(grpUser.getUser().getInfo())
                .build()).collect(Collectors.toList());

        this.members = grp.getUsers().stream().filter(grpUser -> RoleCode.MEMBER.equals(grpUser.getRole())).map(grpUser
                -> User.builder()
                .id(grpUser.getUser().getId())
                .email(grpUser.getUser().getEmail())
                .name(grpUser.getUser().getName())
                .info(grpUser.getUser().getInfo())
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
