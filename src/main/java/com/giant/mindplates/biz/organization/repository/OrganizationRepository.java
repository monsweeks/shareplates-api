package com.giant.mindplates.biz.organization.repository;

import com.giant.mindplates.biz.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Long countByUseYnTrueAndPublicYnTrue();

    @Query("select new Organization(o.id, o.name, o.publicYn) from Organization o where o.useYn = 1 and o.publicYn = 1")
    List<Organization> findPublicOrganization();

    @Query("SELECT new Organization(o.id, o.name, o.publicYn) FROM Organization o INNER JOIN o.users u where o.useYn = :useYn and u.id = :userId")
    List<Organization> findUserOrganization(@Param("useYn") Boolean useYn, @Param("userId") Long userId);

}

