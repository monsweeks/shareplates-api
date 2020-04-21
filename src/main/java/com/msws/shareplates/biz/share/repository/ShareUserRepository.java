package com.msws.shareplates.biz.share.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.common.code.SocketStatusCode;

public interface ShareUserRepository extends JpaRepository<ShareUser, Long> {
	

	Optional<ShareUser> findByShareIdAndUserIdAndStatus(long shareId, long UserId, SocketStatusCode status);

	ShareUser findByShareIdAndUserId(long shareId, long userId);
	
	Optional<ShareUser> findByUuid(String uuid);
	
	@Modifying
    @Query("UPDATE ShareUser s SET s.status = :status WHERE s.uuid = :uuid")
	void updateStatusByUudi(@Param("status") SocketStatusCode code, @Param("uuid")String uuid);

}

