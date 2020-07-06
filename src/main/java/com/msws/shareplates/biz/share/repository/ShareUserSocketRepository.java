package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.ShareUserSocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShareUserSocketRepository extends JpaRepository<ShareUserSocket, Long> {

    @Modifying
    @Query("DELETE FROM ShareUserSocket sus WHERE sus.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);

    @Modifying
    @Query("DELETE FROM ShareUserSocket sus WHERE sus.shareUser.id IN (SELECT su.id FROM ShareUser su WHERE su.share.id = :shareId AND su.user.id = :userId)")
    void deleteShareUserSocket(@Param("shareId") long shareId, @Param("userId") long userId);

    @Query("SELECT new java.lang.Long(count(sus.id)) FROM ShareUserSocket sus WHERE sus.shareUser.share.id = :shareId AND sus.shareUser.user.id = :userId")
    Long countSessionByShareIdAndUserId(@Param("shareId") Long shareId, @Param("userId") Long userId);

    Long countByShareUserShareIdAndShareUserUserIdAndFocusYnTrue(Long shareId, Long userId);

    ShareUserSocket findBySessionId(String sessionId);

}

