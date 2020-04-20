package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, s.currentChapter.id, s.currentChapter.title, s.currentPage.id, s.currentPage.title, s.lastOpenDate, s.lastCloseDate, s.startedYn, s.topic.id, s.adminUser.id ) " +
            " FROM Share s " +
            " WHERE s.topic.id = :topicId AND s.adminUser.id = :userId")
    List<Share> selectShareListByTopicId(@Param("topicId") Long topicId, @Param("userId") Long userId);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, s.currentChapter.id, s.currentChapter.title, s.currentPage.id, s.currentPage.title, s.lastOpenDate, s.lastCloseDate, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info ) " +
            " FROM Share s " +
            " WHERE s.openYn = true AND (s.privateYn = false OR s.adminUser.id = :userId)")
    List<Share> selectOpenShareList(@Param("userId") Long userId);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, s.currentChapter.id, s.currentChapter.title, s.currentPage.id, s.currentPage.title, s.lastOpenDate, s.lastCloseDate, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info ) " +
            " FROM Share s " +
            " WHERE s.id = :shareId")
    Share selectShareInfo(@Param("shareId") Long shareId);


}

