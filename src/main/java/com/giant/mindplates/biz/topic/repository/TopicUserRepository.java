package com.giant.mindplates.biz.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.giant.mindplates.biz.topic.entity.TopicUser;
import com.giant.mindplates.biz.topic.entity.TopicUserId;

public interface TopicUserRepository extends JpaRepository<TopicUser, TopicUserId>{
	
//	@Query("DELETE FROM topicUser t WHERE t.topicid = :topicId")
	void deleteByTopicId(long topicId);
}
