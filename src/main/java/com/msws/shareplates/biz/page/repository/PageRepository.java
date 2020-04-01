package com.msws.shareplates.biz.page.repository;

import com.msws.shareplates.biz.page.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findByChapterTopicIdAndChapterIdOrderByOrderNo(long topicId, long chapterId);

    Optional<Page> findByChapterTopicIdAndChapterIdAndId(long topicId, long chapterId, long id);

    @Modifying
    @Query("UPDATE Page p SET p.orderNo = :orderNo WHERE p.id = :pageId AND p.chapter.id = (SELECT c.id FROM Chapter c INNER JOIN Topic t ON c.topic.id = t.id WHERE t.id = :topicId AND c.id = :chapterId)")
    void updatePageOrder(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId, @Param("pageId") Long pageId, @Param("orderNo") Integer orderNo);

    @Modifying
    @Query("DELETE FROM Page p WHERE p.id = :pageId AND p.chapter.id = (SELECT c.id FROM Chapter c INNER JOIN Topic t ON c.topic.id = t.id WHERE t.id = :topicId AND c.id = :chapterId)")
    void deletePageById(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId, @Param("pageId") Long pageId);

}
