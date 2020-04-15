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
    private String currentChapterTitle;
    private Long currentPageId;
    private String currentPageTitle;
    private LocalDateTime lastOpenDate;
    private LocalDateTime lastCloseDate;
    private Long topicId;
    private String topicName;
    private Boolean startedYn;
    private Long adminUserId;
    private String adminUserEmail;
    private String adminUserName;
    private String adminUserInfo;


    public ShareResponse(com.msws.shareplates.biz.share.entity.Share share) {
        this.id = share.getId();
        this.name = share.getName();
        this.openYn = share.getOpenYn();
        this.privateYn = share.getPrivateYn();
        this.memo = share.getMemo();
        this.accessCode = share.getAccessCode();
        this.currentChapterId = share.getCurrentChapter().getId();
        this.currentChapterTitle = share.getCurrentChapter().getTitle();
        this.currentPageId = share.getCurrentPage().getId();
        this.currentPageTitle = share.getCurrentPage().getTitle();
        this.lastOpenDate = share.getLastOpenDate();
        this.lastCloseDate = share.getLastCloseDate();
        this.topicId = share.getTopic().getId();
        this.topicName = share.getTopic().getName();
        this.startedYn = share.getStartedYn();
        this.adminUserId = share.getAdminUser().getId();
        this.adminUserEmail = share.getAdminUser().getEmail();
        this.adminUserName = share.getAdminUser().getName();
        this.adminUserInfo = share.getAdminUser().getInfo();
    }

}
