package com.giant.mindplates.biz.organization.repository;

import com.giant.mindplates.biz.organization.entity.Organization;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Long countByUseYnTrueAndPublicYnTrue();

    @Query("select new Organization(o.id, o.name, o.publicYn) from Organization o where o.useYn = 1 and o.publicYn = 1")
    List<Organization> findPublicOrganization();

    @Query("SELECT new Organization(o.id, o.name, o.publicYn) FROM OrganizationUser ou INNER JOIN ou.user u INNER JOIN ou.organization o where o.useYn = :useYn and u.id = :userId")
    List<Organization> findUserOrganization(@Param("useYn") Boolean useYn, @Param("userId") Long userId);

    @Query("SELECT new Organization(t.id, t.name, t.description, t.publicYn, t.creationDate, COUNT(u.id), (SELECT COUNT(t.id) FROM Topic t WHERE t.organization.id = t.id), (SELECT iou.role FROM OrganizationUser iou WHERE iou.user.id = :userId AND iou.organization.id = t.id)) " +
            " FROM OrganizationUser ou INNER JOIN ou.user u INNER JOIN ou.organization t " +
            " WHERE t.useYn = :useYn AND t.name LIKE CONCAT(:searchWord, '%') AND t.id IN (SELECT iou.organization.id FROM OrganizationUser iou WHERE iou.user.id = :userId) " +
            " GROUP BY t.id, t.name, t.publicYn, t.creationDate ")
    List<Organization> findOrganizationListByUser(@Param("userId") Long userId, @Param("searchWord")  String searchWord, @Param("useYn") Boolean useYn, Sort sort);

}

