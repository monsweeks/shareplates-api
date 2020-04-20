package com.msws.shareplates.biz.share.vo.response;

import com.msws.shareplates.biz.share.entity.AccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessCodeResponse extends RepresentationModel<AccessCodeResponse> {

    private Long id;
    private String code;
    private Long shareId;
    private Long userId;

    public AccessCodeResponse(AccessCode accessCode) {
        this.id = accessCode.getId();
        this.code = accessCode.getCode();
        this.shareId = accessCode.getShare() != null ? accessCode.getShare().getId() : null;
        this.userId = accessCode.getUser() != null ? accessCode.getUser().getId() : null;
    }
}
