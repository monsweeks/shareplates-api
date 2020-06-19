package com.msws.shareplates.biz.grp.repository;

import com.msws.shareplates.biz.grp.entity.GrpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GrpUserRepository extends JpaRepository<GrpUser, Long> {

    GrpUser findByUserIdAndGrpId(Long userId, Long grpId);

    @Modifying
    @Query("DELETE FROM GrpUser gu WHERE gu.user.id = :userId")
    void deleteAllGrpUserByUserId(@Param("userId") Long userId);

}

