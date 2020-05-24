package com.msws.shareplates.biz.grp.vo.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;

import com.msws.shareplates.common.code.RoleCode;

import lombok.Builder;
import lombok.Data;

@Data
public class GrpsResponse extends RepresentationModel<GrpsResponse> {

    private List<Grp> grps;

    public GrpsResponse(List<com.msws.shareplates.biz.grp.entity.Grp> grps) {
        this.grps = grps.stream().map(grp
                -> Grp.builder()
                .id(grp.getId())
                .name(grp.getName())
                .description(grp.getDescription())
                .publicYn(grp.getPublicYn())
                .userCount(grp.getUserCount())
                .topicCount(grp.getTopicCount())
                .role(grp.getRole())
                .admins(grp.getUsers().stream().filter(grpUser -> RoleCode.ADMIN.equals(grpUser.getRole())).map(grpUser
                        -> User.builder()
                        .id(grpUser.getUser().getId())
                        .email(grpUser.getUser().getName())
                        .name(grpUser.getUser().getName())
                        .info(grpUser.getUser().getInfo())
                        .build()).collect(Collectors.toList()))
                .members(grp.getUsers().stream().filter(grpUser -> RoleCode.MEMBER.equals(grpUser.getRole())).map(grpUser
                        -> User.builder()
                        .id(grpUser.getUser().getId())
                        .email(grpUser.getUser().getName())
                        .name(grpUser.getUser().getName())
                        .info(grpUser.getUser().getInfo())
                        .build()).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());

    }

    @Builder
    @Data
    public static class Grp extends RepresentationModel<Grp> {
        private Long id;
        private String name;
        private String description;
        private Boolean publicYn;
        private Long userCount;
        private Long topicCount;
        private RoleCode role;
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
