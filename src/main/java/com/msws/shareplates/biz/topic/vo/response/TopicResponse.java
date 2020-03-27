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
    private Long organizationId;
    private String organizationName;
    private Integer iconIndex;
    private Boolean privateYn;
    private List<TopicResponse.User> users;
    private LocalDateTime creationDate;

    public TopicResponse(Topic topic) {
        this.id = topic.getId();
        this.name = topic.getName();
        this.summary = topic.getSummary();
        this.organizationId = topic.getOrganizationId();
        this.organizationName = topic.getOrganization().getName();
        this.iconIndex = topic.getIconIndex();
        this.privateYn = topic.getPrivateYn();
        this.creationDate = topic.getCreationDate();
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
