package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.Share;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.startedYn, s.topic.id, s.adminUser.id ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id" +
            " WHERE s.topic.id = :topicId AND s.adminUser.id = :userId")
    List<Share> selectShareListByTopicIdAndUserId(@Param("topicId") Long topicId, @Param("userId") Long userId);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info, SUM(CASE WHEN su.status='ONLINE' THEN 1 ELSE 0 END), SUM(CASE WHEN su.status='OFFLINE' THEN 1 ELSE 0 END) ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id LEFT OUTER JOIN ShareUser su ON s.id = su.share.id " +
            " WHERE s.topic.id = :topicId " +
            " GROUP BY s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info ORDER BY s.id DESC "
    )
    List<Share> selectShareListByTopicId11(@Param("topicId") Long topicId);

    List<Share> findAllByTopicId(@Param("topicId") Long topicId);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info, SUM(CASE WHEN su.status='ONLINE' THEN 1 ELSE 0 END), SUM(CASE WHEN su.status='OFFLINE' THEN 1 ELSE 0 END) ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id LEFT OUTER JOIN ShareUser su ON s.id = su.share.id " +
            " WHERE s.openYn = true AND (s.privateYn = false OR s.adminUser.id = :userId) AND s.name LIKE CONCAT(:name, '%') " +
            " GROUP BY s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info "
    )
    List<Share> selectOpenShareList(@Param("userId") Long userId, @Param("name") String name, Sort sort);

    List<Share> findAllByOpenYnTrueAndPrivateYnFalseOrAdminUserIdAndNameLike(@Param("adminUserId") Long userId, @Param("name") String name, Sort sort);

    @Query(" SELECT new java.lang.Long(count(s.id)) " +
            " FROM Share s WHERE s.openYn = true AND (s.privateYn = false OR s.adminUser.id = :userId) "
    )
    Long selectOpenShareCount(@Param("userId") Long userId);

    @Query(" SELECT new Share(s.id, s.name, s.openYn, s.privateYn, s.memo, s.accessCode, c.id, c.title, p.id, p.title, s.startedYn, s.topic.id, s.topic.name, s.adminUser.id, s.adminUser.email, s.adminUser.name, s.adminUser.info ) " +
            " FROM Share s LEFT OUTER JOIN Page p ON s.currentPage.id = p.id LEFT OUTER JOIN Chapter c ON s.currentChapter.id = c.id" +
            " WHERE s.id = :shareId")
    Share selectShareInfo(@Param("shareId") Long shareId);

    @Modifying
    @Query("UPDATE Share s SET s.currentPage.id = null WHERE s.topic.id = :topicId AND s.currentChapter.id = :chapterId AND s.currentPage.id = :pageId")
    void updateCurrentPageNull(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId, @Param("pageId") Long pageId);

    @Modifying
    @Query("UPDATE Share s SET s.currentChapter.id = null, s.currentPage.id = null WHERE s.topic.id = :topicId AND s.currentChapter.id = :chapterId")
    void updateCurrentChapterAndPageNull(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId);

    Optional<Share> findByAccessCode(String accessCode);

    Optional<Share> findByIdAndAccessCode(long shareId, String accessCode);


}

