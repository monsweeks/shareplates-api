package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.ShareTimeBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShareTimeBucketRepository extends JpaRepository<ShareTimeBucket, Long> {
    @Modifying
    @Query("DELETE FROM ShareTimeBucket stb WHERE stb.share.id = :shareId")
    void deleteAllByShareId(@Param("shareId") Long shareId);

}

