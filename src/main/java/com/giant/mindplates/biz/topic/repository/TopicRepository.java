package com.giant.mindplates.biz.topic.repository;

import com.giant.mindplates.biz.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Long countByOrganizationIdAndName(Long organizationId, String name);
    List<Topic> findAllByUseYnTrue();
    Optional<Topic> findByIdAndUseYnTrue(Long id);

}

