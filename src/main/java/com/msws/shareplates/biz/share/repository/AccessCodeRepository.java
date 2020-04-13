package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.AccessCode;
import com.msws.shareplates.biz.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {

    @Query("SELECT new java.lang.Long(count(ac.id)) FROM AccessCode ac WHERE ac.code = :accessCode")
    Long countByAccessCode(@Param("accessCode") String accessCode);

    Optional<AccessCode> findByIdAndUserId(Long id, Long userId);

}

