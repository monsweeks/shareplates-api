package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, s.currentChapter.id, s.currentChapter.title, s.currentPage.id, s.currentPage.title, s.adminUser.id, s.topic.id, s.lastOpenDate, s.lastCloseDate ) " +
            " FROM Share s " +
            " WHERE s.topic.id = :topicId AND s.adminUser.id = :userId")
    List<Share> selectShareListByTopicId(@Param("topicId") Long topicId, @Param("userId") Long userId);

}

