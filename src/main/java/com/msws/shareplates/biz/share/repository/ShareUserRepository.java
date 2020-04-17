package com.msws.shareplates.biz.share.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msws.shareplates.biz.share.entity.ShareUser;

public interface ShareUserRepository extends JpaRepository<ShareUser, Long> {
	

	Optional<ShareUser> findByShareIdAndUserId(long shareId, long UserId);
	

}

