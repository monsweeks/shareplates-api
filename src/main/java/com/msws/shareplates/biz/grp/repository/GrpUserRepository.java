package com.msws.shareplates.biz.grp.repository;

import com.msws.shareplates.biz.grp.entity.GrpUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrpUserRepository extends JpaRepository<GrpUser, Long> {

    GrpUser findByUserIdAndGrpId(Long userId, Long grpId);

}

