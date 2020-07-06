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

	@Modifying
	@Query("UPDATE ShareUser s SET s.focusYn = :focus WHERE s.id = :id ")
	void updateFocusById(@Param("id") Long id, @Param("focus") Boolean focus);

	@Modifying
	@Query("DELETE FROM ShareUser su WHERE su.id= :id ")
	void deleteShareUserById(@Param("id") Long id);

	void deleteAllShareUserByUserId(@Param("userId") Long userId);

	List<ShareUser> findAllByShareId(long shareId);

	@Query("SELECT new java.lang.Long(count(su.id)) FROM ShareUser su WHERE su.share.id = :shareId AND su.user.id = :userId AND su.banYn = 1")
	Long countByShareUserBan(@Param("shareId") Long shareId, @Param("userId") Long userId);

}

