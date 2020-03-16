package com.giant.mindplates.biz.organization.vo.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class OrganizationRequest {

    private Long id;
    private String name;
    private String description;
    private Boolean publicYn;
    private Long userCount;
    private Long topicCount;
    private String role;
    private LocalDateTime creationDate;
    private List<User> admins;
    private List<User> members;


    @Data
    public static class User {
        private Long id;
        private String email;
        private String name;
        private String info;
    }

}
