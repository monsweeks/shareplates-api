package com.msws.shareplates.biz.user.vo.request;

import com.msws.shareplates.common.code.RoleCode;
import lombok.Data;

@Data
public class UserRequest {

    private Long id;
    private String email;
    private String name;
    private String info;
    private String dateTimeFormat;
    private String language;
    private Boolean registered;
    private RoleCode roleCode;
    private RoleCode activeRoleCode;


}
