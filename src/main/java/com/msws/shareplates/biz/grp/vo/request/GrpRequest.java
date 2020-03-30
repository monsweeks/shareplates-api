package com.msws.shareplates.biz.grp.vo.request;

import java.time.LocalDateTime;
import java.util.List;

import com.msws.shareplates.common.code.AuthCode;

import lombok.Data;


@Data
public class GrpRequest {

    private Long id;
    private String name;
    private String description;
    private Boolean publicYn;
    private Long userCount;
    private Long topicCount;
    private AuthCode role;
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
