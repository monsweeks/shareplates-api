package com.msws.shareplates.biz.share.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShareStatusResponse extends RepresentationModel<ShareStatusResponse> {

    private Long id;
    private Boolean openYn;
    private Boolean privateYn;

    public ShareStatusResponse(com.msws.shareplates.biz.share.entity.Share share) {
        this.id = share.getId();
        this.openYn = share.getOpenYn();
        this.privateYn = share.getPrivateYn();
    }

}
