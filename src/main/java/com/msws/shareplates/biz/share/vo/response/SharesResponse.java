package com.msws.shareplates.biz.share.vo.response;

import com.msws.shareplates.biz.share.entity.Share;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SharesResponse extends RepresentationModel<SharesResponse> {

    private List<ShareResponse> shares;

    public SharesResponse(List<Share> shares) {
        this.shares = shares.stream().map(share -> new ShareResponse(share)).collect(Collectors.toList());
    }
}
