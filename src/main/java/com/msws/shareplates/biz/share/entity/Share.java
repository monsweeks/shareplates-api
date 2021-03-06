package com.msws.shareplates.biz.share.entity;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.share.vo.request.ShareRequest;
import com.msws.shareplates.biz.topic.entity.Topic;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "share")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Share extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "name")
    private String name;

    @Column(name = "open_yn")
    private Boolean openYn;

    @Column(name = "private_yn")
    private Boolean privateYn;

    @Length(max = 255)
    @Column(name = "description")
    private String description;

    @Column(columnDefinition = "text", name = "memo")
    private String memo;

    @Column(name = "access_code")
    private String accessCode;

    @OneToOne
    @JoinColumn(name = "current_chapter_id")
    private Chapter currentChapter;

    @OneToOne
    @JoinColumn(name = "current_page_id")
    private Page currentPage;

    @OneToOne
    @JoinColumn(name = "admin_user_id")
    private User adminUser;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT)
    private List<ShareUser> shareUsers;

    @Column(name = "started_yn")
    private Boolean startedYn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareTimeBucket> shareTimeBuckets = new ArrayList<>();

    @Transient
    private Long onLineUserCount;

    @Transient
    private Long offLineUserCount;

    public Share(Long id, String name, Boolean openYn, Boolean privateYn, String memo, String accessCode, Long currentChapterId, String chapterTitle, Long currentPageId, String pageTitle, Boolean startedYn, Long topicId, Long adminUserId) {
        this.id = id;
        this.name = name;
        this.openYn = openYn;
        this.privateYn = privateYn;
        this.memo = memo;
        this.accessCode = accessCode;
        this.currentChapter = Chapter.builder().id(currentChapterId).title(chapterTitle).build();
        this.currentPage = Page.builder().id(currentPageId).title(pageTitle).build();
        this.topic = Topic.builder().id(topicId).build();
        this.startedYn = startedYn;
        this.adminUser = User.builder().id(adminUserId).build();
    }

    public Share(Long id, String name, Boolean openYn, Boolean privateYn, String memo, String accessCode, Long currentChapterId, String chapterTitle, Long currentPageId, String pageTitle, Boolean startedYn, Long topicId, String topicName, Long adminUserId, String adminUserEmail, String adminUserName, String adminUserInfo) {
        this.id = id;
        this.name = name;
        this.openYn = openYn;
        this.privateYn = privateYn;
        this.memo = memo;
        this.accessCode = accessCode;
        this.currentChapter = Chapter.builder().id(currentChapterId).title(chapterTitle).build();
        this.currentPage = Page.builder().id(currentPageId).title(pageTitle).build();
        this.topic = Topic.builder().id(topicId).name(topicName).build();
        this.startedYn = startedYn;
        this.adminUser = User.builder().id(adminUserId).email(adminUserEmail).name(adminUserName).info(adminUserInfo).build();
    }

    public Share(Long id, String name, Boolean openYn, Boolean privateYn, String memo, String accessCode, Long currentChapterId, String chapterTitle, Long currentPageId, String pageTitle, Boolean startedYn, Long topicId, String topicName, Long adminUserId, String adminUserEmail, String adminUserName, String adminUserInfo, Long onLineUserCount, Long offLineUserCount) {
        this.id = id;
        this.name = name;
        this.openYn = openYn;
        this.privateYn = privateYn;
        this.memo = memo;
        this.accessCode = accessCode;
        this.currentChapter = Chapter.builder().id(currentChapterId).title(chapterTitle).build();
        this.currentPage = Page.builder().id(currentPageId).title(pageTitle).build();
        this.topic = Topic.builder().id(topicId).name(topicName).build();
        this.startedYn = startedYn;
        this.adminUser = User.builder().id(adminUserId).email(adminUserEmail).name(adminUserName).info(adminUserInfo).build();
        this.onLineUserCount = onLineUserCount;
        this.offLineUserCount = offLineUserCount;
    }

    public Share(ShareRequest shareRequest) {
        this.id = shareRequest.getId();
        this.merge(shareRequest);
    }

    public void merge(ShareRequest shareRequest) {
        this.name = shareRequest.getName();
        this.openYn = shareRequest.getOpenYn();
        this.privateYn = shareRequest.getPrivateYn();
        this.memo = shareRequest.getMemo();
        this.description = shareRequest.getDescription();
        this.accessCode = shareRequest.getAccessCode();
        this.currentChapter = Chapter.builder().id(shareRequest.getCurrentChapterId()).build();
        this.currentPage = Page.builder().id(shareRequest.getCurrentPageId()).build();
        this.adminUser = User.builder().id(shareRequest.getAdminUserId()).build();
        this.topic = Topic.builder().id(shareRequest.getTopicId()).build();
    }

}
