package com.msws.shareplates.biz.user.vo.response;

import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse extends RepresentationModel<UserResponse> {

    private Long id;
    private String email;
    private String name;
    private String info;
    private RoleCode shareRoleCode;
    private SocketStatusCode status;
    private String message;
    private Boolean banYn;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.info = user.getInfo();
    }

    public UserResponse(User user, RoleCode roleCode, SocketStatusCode status, String message, Boolean banYn) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.info = user.getInfo();
        this.shareRoleCode = roleCode;
        this.status = status;
        this.message = message;
        this.banYn = banYn;
    }

}
