package com.msws.shareplates.biz.topic.repository;

import com.msws.shareplates.biz.topic.entity.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Long countByOrganizationIdAndName(Long organizationId, String name);

    Long countByOrganizationId(Long organizationId);

    Optional<Topic> findByIdAndUseYnTrue(Long id);

    @Query(" SELECT new Topic(t.id, t.name, t.summary, t.iconIndex, t.privateYn, t.chapterCount, t.pageCount) " +
            " FROM Topic t " +
            " WHERE t.useYn = 1 AND t.organizationId = :organizationId AND t.name LIKE CONCAT(:searchWord, '%') AND (t.id in (SELECT tu.topic.id FROM TopicUser tu WHERE tu.user.id = :userId) OR t.privateYn = 0)")
    List<Topic> findTopicList(@Param("userId") Long userId, @Param("organizationId") Long organizationId, @Param("searchWord") String searchWord, Sort sort);

}

