package com.msws.shareplates.biz.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msws.shareplates.biz.topic.entity.TopicUser;
import com.msws.shareplates.biz.topic.entity.TopicUserId;

public interface TopicUserRepository extends JpaRepository<TopicUser, TopicUserId>{
	

}
