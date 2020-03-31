package com.msws.shareplates.biz.topic.entity;

import com.msws.shareplates.biz.grp.entity.Grp;
import com.msws.shareplates.biz.topic.vo.request.TopicRequest;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Table(name = "topic")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Topic extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "text", name = "summary")
    private String summary;

    @Column(name = "use_yn")
    private Boolean useYn;

    @Column(name = "grp_id")
    private Long grpId;

    @Column(name = "icon_index")
    private Integer iconIndex;

    @Column(name = "private_yn")
    private Boolean privateYn;

    @Column(name = "chapter_count")
    private Integer chapterCount;

    @Column(name = "page_count")
    private Integer pageCount;

    @ManyToOne
    @JoinColumn(name = "grp_id", insertable = false, updatable = false)
    private Grp grp;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT)
    private List<TopicUser> topicUsers;

    public Topic(Long id, String name, String summary, Integer iconIndex, Boolean privateYn, Integer chapterCount, Integer pageCount) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.iconIndex = iconIndex;
        this.privateYn = privateYn;
        this.chapterCount = chapterCount;
        this.pageCount = pageCount;
    }

    public Topic(TopicRequest topicRequest) {
        this.id = topicRequest.getId();
        this.name = topicRequest.getName();
        this.summary = topicRequest.getSummary();
        this.iconIndex = topicRequest.getIconIndex();
        this.privateYn = topicRequest.getPrivateYn();
        this.grpId = topicRequest.getGrpId();
        this.useYn = true;
        this.chapterCount = 0;
        this.pageCount = 0;

        List<TopicUser> topicUsers = topicRequest.getUsers().stream().map(user
                -> TopicUser.builder()
                .topic(this)
                .user(User.builder().id(user.getId()).build())
                .build())
                .collect(Collectors.toList());

        this.setTopicUsers(topicUsers);
    }

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(value = FetchMode.SUBSELECT)
//    private List<Chapter> chapters;
}
