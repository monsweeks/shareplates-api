package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {

    @Query("SELECT new java.lang.Long(count(ac.id)) FROM AccessCode ac WHERE ac.code = :accessCode")
    Long countByAccessCode(@Param("accessCode") String accessCode);

    @Modifying
    @Query("DELETE FROM AccessCode ac WHERE ac.share.id = :shareId")
    void deleteAccessCodeByShareId(@Param("shareId") Long shareId);

    Optional<AccessCode> findByIdAndUserId(Long id, Long userId);

    Optional<AccessCode> findByCode(String code);

}

