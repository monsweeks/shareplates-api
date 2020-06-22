package com.msws.shareplates.biz.share.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareRequest {
    private Long id;
    private Long topicId;
    private String name;
    private String memo;
    private Boolean privateYn;
    private Boolean openYn;
    private Long currentChapterId;
    private Long currentPageId;
    private String accessCode;
    private Long accessCodeId;
    private Long adminUserId;

}
