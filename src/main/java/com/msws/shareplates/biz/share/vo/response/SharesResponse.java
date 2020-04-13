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
                .currentPageId(share.getCurrentPage().getId())
                .adminUserId(share.getAdminUser().getId())
                .topicId(share.getTopic().getId())
                .lastOpenDate(share.getLastOpenDate())
                .lastCloseDate(share.getLastCloseDate())
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
        private Long currentPageId;
        private Long adminUserId;
        private Long topicId;
        private LocalDateTime lastOpenDate;
        private LocalDateTime lastCloseDate;
    }
}
