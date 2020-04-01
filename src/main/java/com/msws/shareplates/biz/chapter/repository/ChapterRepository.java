package com.msws.shareplates.biz.chapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.msws.shareplates.biz.chapter.entity.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findByTopicIdOrderByOrderNo(long topicId);


    @Modifying
    @Query("UPDATE Chapter c SET c.orderNo = :orderNo WHERE c.topic.id = :topicId AND c.id = :chapterId")
    void updateChapterOrder(@Param("topicId") Long topicId, @Param("chapterId") Long chapterId, @Param("orderNo") Integer orderNo);
    
    Optional<Chapter> findByIdAndTopicId(long id, long topicId);
}
