package com.msws.shareplates.biz.page.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PageOrdersRequest {

    private Long topicId;

    private List<Page> pages;

    public List<com.msws.shareplates.biz.page.entity.Page> buildPageListEntity() {
        return pages.stream()
                .map(page -> com.msws.shareplates.biz.page.entity.Page.builder()
                        .id(page.getId())
                        .orderNo(page.getOrderNo())
                        .build()).collect(Collectors.toList());
    }

    @Data
    public static class Page {
        private Long id;
        private Integer orderNo;
    }
}
