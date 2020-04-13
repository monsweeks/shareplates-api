package com.msws.shareplates.biz.share.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShareResponse extends RepresentationModel<ShareResponse> {

    private Long id;
    private String name;
    private Boolean openYn;
    private Boolean privateYn;
    private String memo;
    private String accessCode;
    private Long currentChapterId;
    private Long currentPageId;
    private Long adminUserId;
    private Long topicId;
    private LocalDateTime lastOpenDate;
    private LocalDateTime lastCloseDate;

    public ShareResponse(com.msws.shareplates.biz.share.entity.Share share) {
        this.id = share.getId();
        this.name = share.getName();
        this.openYn = share.getOpenYn();
        this.privateYn = share.getPrivateYn();
        this.memo = share.getMemo();
        this.accessCode = share.getAccessCode();
        this.currentChapterId = share.getCurrentChapter().getId();
        this.currentPageId = share.getCurrentPage().getId();
        this.adminUserId = share.getAdminUser().getId();
        this.topicId = share.getTopic().getId();
        this.lastOpenDate = share.getLastOpenDate();
        this.lastCloseDate = share.getLastCloseDate();

    }

}
