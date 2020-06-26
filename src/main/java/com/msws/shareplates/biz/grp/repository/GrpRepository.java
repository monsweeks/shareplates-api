package com.msws.shareplates.biz.grp.repository;

import com.msws.shareplates.biz.grp.entity.Grp;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrpRepository extends JpaRepository<Grp, Long> {
    Long countByUseYnTrueAndPublicYnTrue();
    
    @Cacheable(value="groupCache")
    @Query("select new Grp(o.id, o.name, o.publicYn) from Grp o where o.useYn = 1 and o.publicYn = 1")
    List<Grp> findPublicGrp();

    @Cacheable(value="groupCache")
    @Query("SELECT new Grp(o.id, o.name, o.publicYn) FROM GrpUser ou INNER JOIN ou.user u INNER JOIN ou.grp o where o.useYn = :useYn and u.id = :userId")
    List<Grp> findUserGrp(@Param("useYn") Boolean useYn, @Param("userId") Long userId);

    @Query("SELECT new Grp(t.id, t.name, t.description, t.publicYn, t.creationDate, COUNT(u.id), (SELECT COUNT(t.id) FROM Topic t WHERE t.grp.id = ou.grp.id), (SELECT iou.role FROM GrpUser iou WHERE iou.user.id = :userId AND iou.grp.id = t.id)) " +
            " FROM GrpUser ou INNER JOIN ou.user u INNER JOIN ou.grp t " +
            " WHERE t.useYn = :useYn AND t.name LIKE CONCAT(:searchWord, '%') AND t.id IN (SELECT iou.grp.id FROM GrpUser iou WHERE iou.user.id = :userId) " +
            " GROUP BY t.id, t.name, t.publicYn, t.creationDate ")
    List<Grp> findGrpListByUser(@Param("userId") Long userId, @Param("searchWord")  String searchWord, @Param("useYn") Boolean useYn, Sort sort);

    @Query("SELECT new java.lang.Boolean(o.publicYn) FROM Grp o WHERE id = :grpId")
    Boolean isPublicGrp(@Param("grpId") Long grpId);

    @Query("SELECT ou.role FROM Grp o INNER JOIN GrpUser ou ON o.id = ou.grp.id WHERE  o.id = :grpId AND ou.user.id = :userId")
    String findUserGrpRole(@Param("grpId") Long grpId, @Param("userId") Long userId);
}

