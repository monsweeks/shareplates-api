package com.giant.mindplates.biz.user.repository;

import com.giant.mindplates.biz.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByActivationToken(String activationToke);

    Long countByEmailAndUseYn(String email, Boolean useYn);

    @Query("SELECT new User(u.id, u.email, u.name, u.info, u.dateTimeFormat, u.language) FROM User u INNER JOIN u.organizations o WHERE o.useYn = 1 AND u.useYn = 1 AND u.allowSearchYn = 1 AND o.id = :organizationId AND (u.name LIKE CONCAT('%' ,:condition) OR u.email LIKE CONCAT('%' ,:condition) )")
    List<User> selectByOrganization(@Param("organizationId") Long organizationId, @Param("condition") String condition);

    @Query("SELECT new User(u.id, u.email, u.name, u.info, u.dateTimeFormat, u.language) FROM User u WHERE u.useYn = 1 AND u.allowSearchYn = 1 AND (u.name LIKE CONCAT(:condition, '%') OR u.email LIKE CONCAT(:condition, '%') )")
    List<User> selectByName(@Param("condition") String condition);
}

