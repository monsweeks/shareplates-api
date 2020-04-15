package com.msws.shareplates.biz.share.vo.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SharesResponse extends RepresentationModel<SharesResponse> {

    private List<Share> shares;

    public SharesResponse(List<com.msws.shareplates.biz.share.entity.Share> shares) {
        this.shares = shares.stream().map(share
                -> Share.builder()
                .id(share.getId())
                .name(share.getName())
                .openYn(share.getOpenYn())
                .privateYn(share.getPrivateYn())
                .memo(share.getMemo())
                .accessCode(share.getAccessCode())
                .currentChapterId(share.getCurrentChapter().getId())
                .currentChapterTitle(share.getCurrentChapter().getTitle())
                .currentPageId(share.getCurrentPage().getId())
                .currentPageTitle(share.getCurrentPage().getTitle())
                .lastOpenDate(share.getLastOpenDate())
                .lastCloseDate(share.getLastCloseDate())
                .topicId(share.getTopic().getId())
                .topicName(share.getTopic().getName())
                .adminUserId(share.getAdminUser().getId())
                .adminUserEmail(share.getAdminUser().getEmail())
                .adminUserName(share.getAdminUser().getName())
                .adminUserInfo(share.getAdminUser().getInfo())
                .build()).collect(Collectors.toList());
    }

    @Builder
    @Data
    public static class Share extends RepresentationModel<Share> {
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
        private Long adminUserId;
        private String adminUserEmail;
        private String adminUserName;
        private String adminUserInfo;
    }
}
