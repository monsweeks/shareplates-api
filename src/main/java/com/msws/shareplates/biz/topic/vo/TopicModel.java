package com.msws.shareplates.biz.topic.vo;

import com.msws.shareplates.biz.topic.entity.Topic;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Builder
@Getter
public class TopicModel extends RepresentationModel<TopicModel> {

    private Long id;
    private String name;
    private String summary;
    private Long grpId;
    private String grpName;
    private Integer iconIndex;
    private Boolean privateYn;
    private LocalDateTime creationDate;
    private Integer chapterCount;
    private Integer pageCount;
    private String content;

    public TopicModel buildTopicModel(Topic topic) {
        this.id = topic.getId();
        this.name = topic.getName();
        this.summary = topic.getSummary();
        this.grpId = topic.getGrpId();
        if (topic.getGrp() != null) {
            this.grpName = topic.getGrp().getName();
        }
        this.iconIndex = topic.getIconIndex();
        this.privateYn = topic.getPrivateYn();
        this.creationDate = topic.getCreationDate();
        this.chapterCount = topic.getChapterCount();
        this.pageCount = topic.getPageCount();
        this.content = topic.getContent();

        return this;


    }
}
