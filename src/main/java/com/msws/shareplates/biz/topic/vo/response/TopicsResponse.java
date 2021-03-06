package com.msws.shareplates.biz.topic.vo.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class TopicsResponse extends RepresentationModel<TopicsResponse> {

    private List<Topic> topics;

    public TopicsResponse(List<com.msws.shareplates.biz.topic.entity.Topic> topics) {
        this.topics = topics.stream().map(topic
                -> Topic.builder()
                .id(topic.getId())
                .name(topic.getName())
                .summary(topic.getSummary())
                .privateYn(topic.getPrivateYn())
                .chapterCount(topic.getChapterCount())
                .pageCount(topic.getPageCount())
                .isMember(topic.getIsMember())
                .build()).collect(Collectors.toList());
    }

    @Builder
    @Data
    public static class Topic extends RepresentationModel<Topic> {
        private long id;
        private String name;
        private String summary;
        private Boolean privateYn;
        private Integer chapterCount;
        private Integer pageCount;
        private Boolean isMember;
    }
}
