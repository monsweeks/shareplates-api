package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ShareChapterResponse extends RepresentationModel<ShareChapterResponse> {

    private String title;
    private String summary;
    private int orderNo;
    private boolean useYn;
    private long topicId;
    private long id;
    private int pageCount;
    private String content;
    private List<SharePageResponse> pages;

    public ShareChapterResponse buildChapterModel(Chapter chapter) {

        this.title = chapter.getTitle();
        this.summary = chapter.getSummary();
        this.orderNo = chapter.getOrderNo();
        this.useYn = chapter.getUseYn();
        this.topicId = chapter.getTopic().getId();
        this.id = chapter.getId();
        this.pageCount = chapter.getPageCount();
        this.content = chapter.getContent();
        this.pages = chapter.getPages().stream().map((page -> SharePageResponse.builder().build().buildChapterModel(page))).collect(Collectors.toList());

        return this;
    }
}
