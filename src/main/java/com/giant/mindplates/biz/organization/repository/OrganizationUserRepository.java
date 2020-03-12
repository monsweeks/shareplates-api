package com.giant.mindplates.biz.organization.repository;

import com.giant.mindplates.biz.organization.entity.OrganizationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {

    OrganizationUser findByUserIdAndOrganizationId(Long userId, Long organizationId);

}

