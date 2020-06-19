package com.msws.shareplates.biz.topic.repository;

import com.msws.shareplates.biz.topic.entity.TopicUser;
import com.msws.shareplates.biz.topic.entity.TopicUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicUserRepository extends JpaRepository<TopicUser, TopicUserId> {

    @Modifying
    @Query("DELETE FROM TopicUser tu WHERE tu.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
