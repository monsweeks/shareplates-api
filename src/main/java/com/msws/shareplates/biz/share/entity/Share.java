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
import java.time.LocalDateTime;
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

    @Column(name = "last_open_date")
    private LocalDateTime lastOpenDate;

    @Column(name = "last_close_date")
    private LocalDateTime lastCloseDate;

    public Share(Long id, String name, Boolean openYn, Boolean privateYn, String memo, String accessCode, Long currentChapterId, String chapterTitle, Long currentPageId, String pageTitle, Long adminUserId, Long topicId, LocalDateTime lastOpenDate, LocalDateTime lastCloseDate) {
        this.id = id;
        this.name = name;
        this.openYn = openYn;
        this.privateYn = privateYn;
        this.memo = memo;
        this.accessCode = accessCode;
        this.currentChapter = Chapter.builder().id(currentChapterId).title(chapterTitle).build();
        this.currentPage = Page.builder().id(currentPageId).title(pageTitle).build();
        this.adminUser = User.builder().id(adminUserId).build();
        this.topic = Topic.builder().id(topicId).build();
        this.lastOpenDate = lastOpenDate;
        this.lastCloseDate = lastCloseDate;
    }

    public Share(ShareRequest shareRequest) {
        this.id = shareRequest.getId();
        this.merge(shareRequest);
    }

    public void merge (ShareRequest shareRequest) {
        this.name = shareRequest.getName();
        this.openYn = shareRequest.getOpenYn();
        this.privateYn = shareRequest.getPrivateYn();
        this.memo = shareRequest.getMemo();
        this.accessCode = shareRequest.getAccessCode();
        this.currentChapter = Chapter.builder().id(shareRequest.getCurrentChapterId()).build();
        this.currentPage = Page.builder().id(shareRequest.getCurrentPageId()).build();
        this.adminUser = User.builder().id(shareRequest.getAdminUserId()).build();
        this.topic = Topic.builder().id(shareRequest.getTopicId()).build();
        this.lastOpenDate= shareRequest.getLastOpenDate();
        this.lastCloseDate = shareRequest.getLastCloseDate();
    }

}
