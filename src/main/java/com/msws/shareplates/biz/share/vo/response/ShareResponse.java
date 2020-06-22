package com.msws.shareplates.biz.share.vo.response;

import com.msws.shareplates.biz.user.vo.response.ShareUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

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
    private Long currentChapterId;
    private String currentChapterTitle;
    private Long currentPageId;
    private String currentPageTitle;
    private Long topicId;
    private String topicName;
    private Boolean startedYn;
    private Long adminUserId;
    private String adminUserEmail;
    private String adminUserName;
    private String adminUserInfo;
    private Long onLineUserCount;
    private Long offLineUserCount;
    private List<ShareUserResponse> shareUsers;
    private List<ShareTimeBucketResponse> shareTimeBuckets;

    public ShareResponse(com.msws.shareplates.biz.share.entity.Share share) {
        this.id = share.getId();
        this.name = share.getName();
        this.openYn = share.getOpenYn();
        this.privateYn = share.getPrivateYn();
        this.memo = share.getMemo();
        this.currentChapterId = share.getCurrentChapter() != null ? share.getCurrentChapter().getId() : null;
        this.currentChapterTitle = share.getCurrentChapter() != null ? share.getCurrentChapter().getTitle() : null;
        this.currentPageId = share.getCurrentPage() != null ? share.getCurrentPage().getId() : null;
        this.currentPageTitle = share.getCurrentPage() != null ? share.getCurrentPage().getTitle() : null;
        this.topicId = share.getTopic().getId();
        this.topicName = share.getTopic().getName();
        this.startedYn = share.getStartedYn();
        this.adminUserId = share.getAdminUser().getId();
        this.adminUserEmail = share.getAdminUser().getEmail();
        this.adminUserName = share.getAdminUser().getName();
        this.adminUserInfo = share.getAdminUser().getInfo();
        this.onLineUserCount = share.getOnLineUserCount();
        this.offLineUserCount = share.getOffLineUserCount();
        if (share.getShareUsers() != null) {
            this.setShareUsers(share.getShareUsers().stream()
                    .map(shareUser -> ShareUserResponse.builder()
                            .id(shareUser.getId())
                            .email(shareUser.getUser().getEmail())
                            .name(shareUser.getUser().getName())
                            .info(shareUser.getUser().getInfo())
                            .shareRoleCode(shareUser.getRole())
                            .status(shareUser.getStatus())
                            .banYn(shareUser.getBanYn())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (share.getShareTimeBuckets() != null) {
            this.setShareTimeBuckets(share.getShareTimeBuckets().stream()
                    .map(shareTimeBucket -> ShareTimeBucketResponse.builder()
                            .id(shareTimeBucket.getId())
                            .openDate(shareTimeBucket.getOpenDate())
                            .closeDate(shareTimeBucket.getCloseDate()).build())
                    .collect(Collectors.toList()));
        }


    }

}
