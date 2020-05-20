package com.msws.shareplates.biz.topic.vo.response;

import com.msws.shareplates.biz.topic.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopicResponse extends RepresentationModel<TopicResponse> {

    private Long id;
    private String name;
    private String summary;
    private Long grpId;
    private String grpName;
    private Boolean privateYn;
    private List<TopicResponse.User> users;
    private LocalDateTime creationDate;
    private Integer chapterCount;
    private Integer pageCount;
    private String content;

    public TopicResponse(Topic topic) {
        this.id = topic.getId();
        this.name = topic.getName();
        this.summary = topic.getSummary();
        this.grpId = topic.getGrpId();
        if (topic.getGrp() != null) {
            this.grpName = topic.getGrp().getName();
        }
        this.privateYn = topic.getPrivateYn();
        this.creationDate = topic.getCreationDate();
        this.chapterCount = topic.getChapterCount();
        this.pageCount = topic.getPageCount();
        this.content = topic.getContent();
        this.users = topic.getTopicUsers().stream().map(topicUser
                -> User.builder()
                .id(topicUser.getUser().getId())
                .email(topicUser.getUser().getEmail())
                .name(topicUser.getUser().getName())
                .info(topicUser.getUser().getInfo())
                .build()).collect(Collectors.toList());
    }

    @Builder
    @Data
    public static class User {
        private Long id;
        private String email;
        private String name;
        private String info;
    }

}
