package com.msws.shareplates.biz.chapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msws.shareplates.biz.chapter.entity.Chapter;

public interface ChapterRepository extends  JpaRepository<Chapter, Long>{

	List<Chapter> findByTopicIdOrderByOrderNo(long topicId);
}
