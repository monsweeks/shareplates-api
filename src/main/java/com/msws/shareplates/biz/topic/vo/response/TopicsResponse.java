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
                -> TopicsResponse.Topic.builder()
                .id(topic.getId())
                .name(topic.getName())
                .summary(topic.getSummary())
                .iconIndex(topic.getIconIndex())
                .privateYn(topic.getPrivateYn())
                .chapterCount(topic.getChapterCount())
                .pageCount(topic.getPageCount())
                .build()).collect(Collectors.toList());
    }

    @Builder
    @Data
    public static class Topic extends RepresentationModel<Topic> {
        private long id;
        private String name;
        private String summary;
        private Integer iconIndex;
        private Boolean privateYn;
        private Integer chapterCount;
        private Integer pageCount;
    }
}
