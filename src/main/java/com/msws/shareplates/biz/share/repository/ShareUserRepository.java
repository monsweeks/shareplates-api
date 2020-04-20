package com.msws.shareplates.biz.share.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.common.code.SocketStatusCode;

public interface ShareUserRepository extends JpaRepository<ShareUser, Long> {
	

	Optional<ShareUser> findByShareIdAndUserIdAndStatus(long shareId, long UserId, SocketStatusCode status);
	

}

