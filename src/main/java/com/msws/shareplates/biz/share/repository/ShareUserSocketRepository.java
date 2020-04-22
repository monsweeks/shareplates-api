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

    @Query("SELECT new java.lang.Long(count(sus.id)) FROM ShareUserSocket sus WHERE sus.shareUser.share.id = :shareId AND sus.shareUser.user.id = :userId")
    Long countSessionByShareIdAndUserId(@Param("shareId") Long shareId, @Param("userId") Long userId);

    ShareUserSocket findBySessionId(String sessionId);

}

