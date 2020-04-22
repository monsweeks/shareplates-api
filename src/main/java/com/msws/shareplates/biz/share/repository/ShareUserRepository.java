package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.common.code.SocketStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShareUserRepository extends JpaRepository<ShareUser, Long> {
	

	Optional<ShareUser> findByShareIdAndUserIdAndStatus(long shareId, long UserId, SocketStatusCode status);

	ShareUser findByShareIdAndUserId(long shareId, long userId);

	@Modifying
	@Query("UPDATE ShareUser s SET s.status = :status WHERE s.id = :id ")
	void updateStatusById(@Param("id") Long id, @Param("status") SocketStatusCode status);

	List<ShareUser> findAllByShareId(long shareId);

}

