package com.giant.mindplates.biz.topic.vo;

import com.giant.mindplates.biz.topic.entity.Topic;
import com.giant.mindplates.common.code.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTopic extends RepresentationModel<SimpleTopic> {

    private Long id;
    private String name;
    private String summary;
    private Integer iconIndex;
    private Boolean privateYn;
    private Integer chapterCount;
    private Integer pageCount;
    private StatusCode statusCode;

    public SimpleTopic(Topic topic, StatusCode statusCode) {
        this.id = topic.getId();
        this.name = topic.getName();
        this.summary = topic.getSummary();
        this.iconIndex = topic.getIconIndex();
        this.privateYn = topic.getPrivateYn();
        this.chapterCount = topic.getChapterCount();
        this.pageCount = topic.getPageCount();
        this.statusCode = statusCode;
    }
}
