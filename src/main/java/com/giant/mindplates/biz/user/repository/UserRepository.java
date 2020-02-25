package com.giant.mindplates.biz.user.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giant.mindplates.biz.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);
	User findByActivationToken (String activationToke);
    Long countByEmailAndUseYn(String email, Boolean useYn);
}

