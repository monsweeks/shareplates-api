package com.giant.mindplates.biz.topic.repository;

import com.giant.mindplates.biz.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}

