package com.msws.shareplates.biz.share.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.msws.shareplates.biz.share.entity.Share;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.lastOpenDate, s.lastCloseDate, s.startedYn, s.topic.id, s.adminUser.id ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id" +
            " WHERE s.topic.id = :topicId AND s.adminUser.id = :userId")
    List<Share> selectShareListByTopicId(@Param("topicId") Long topicId, @Param("userId") Long userId);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.lastOpenDate, s.lastCloseDate, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id" +
            " WHERE s.openYn = true AND (s.privateYn = false OR s.adminUser.id = :userId) AND s.name LIKE CONCAT(:name, '%')")
    List<Share> selectOpenShareList(@Param("userId") Long userId, @Param("name") String name, Sort sort);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.lastOpenDate, s.lastCloseDate, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id" +
            " WHERE s.id = :shareId")
    Share selectShareInfo(@Param("shareId") Long shareId);

    @Modifying
    @Query("UPDATE Share s SET s.currentPage.id = null WHERE s.topic.id = :topicId AND s.currentChapter.id = :chapterId AND s.currentPage.id = :pageId")
    void updateCurrentPageNull(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId, @Param("pageId") Long pageId);

    @Modifying
    @Query("UPDATE Share s SET s.currentChapter.id = null, s.currentPage.id = null WHERE s.topic.id = :topicId AND s.currentChapter.id = :chapterId")
    void updateCurrentChapterAndPageNull(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId);

}

