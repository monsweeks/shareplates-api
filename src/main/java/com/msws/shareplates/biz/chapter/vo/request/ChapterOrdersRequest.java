package com.msws.shareplates.biz.chapter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChapterOrdersRequest {

    private Long topicId;

    @ApiModelProperty(value = "챕터 ID, ORDER 리스트", example = "[{id=101,orderNo=1},id=102,orderNo=2]")
    private List<Chapter> chapters;

    public List<com.msws.shareplates.biz.chapter.entity.Chapter> buildChaterListEntity() {
        return chapters.stream()
                .map(chapter -> com.msws.shareplates.biz.chapter.entity.Chapter.builder()
                        .id(chapter.getId())
                        .orderNo(chapter.getOrderNo())
                        .build()).collect(Collectors.toList());
    }

    @Data
    public static class Chapter {
        private Long id;
        private Integer orderNo;
    }
}
