package com.msws.shareplates.biz.topic.repository;

import com.msws.shareplates.biz.topic.entity.Topic;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Long countByGrpIdAndName(Long grpId, String name);

    Long countByGrpIdAndNameAndIdNot(Long grpId, String name, Long id);

    Long countByGrpId(Long grpId);

    Optional<Topic> findByIdAndUseYnTrue(Long id);

    @Query(" SELECT new Topic(t.id, t.name, t.summary, t.privateYn, t.chapterCount, t.pageCount) " +
            " FROM Topic t " +
            " WHERE t.useYn = 1 AND t.grpId = :grpId AND t.name LIKE CONCAT(:searchWord, '%') AND (t.id in (SELECT tu.topic.id FROM TopicUser tu WHERE tu.user.id = :userId) OR t.privateYn = 0)")
    List<Topic> findTopicList(@Param("userId") Long userId, @Param("grpId") Long grpId, @Param("searchWord") String searchWord, Sort sort);

    @Query("SELECT new java.lang.Boolean(t.privateYn) FROM Topic t WHERE t.id = :topicId")
    Boolean isPrivateTopic(@Param("topicId") Long topicId);

    @Query("SELECT new java.lang.Long(count(tu.id)) FROM Topic t INNER JOIN TopicUser tu ON t.id = tu.topic.id WHERE t.id = :topicId AND tu.user.id = :userId")
    Long countByTopicUserCount(@Param("topicId") Long topicId, @Param("userId") Long userId);

    @Query("SELECT new java.lang.Boolean(o.publicYn) FROM Grp o WHERE id = (SELECT t.grpId from Topic t where t.id = :topicId)")
    Boolean isPublicGrp(@Param("topicId") Long topicId);

    @Query("SELECT ou.role FROM Topic t INNER JOIN Grp o ON t.grpId = o.id INNER JOIN GrpUser ou ON o.id = ou.grp.id WHERE  t.id = :topicId AND ou.user.id = :userId")
    String findUserGrpRole(@Param("topicId") Long topicId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Topic t SET t.content = :content WHERE t.id = :topicId")
    void updateTopicContent(@Param("topicId") Long topicId, @Param("content") String content);

}

