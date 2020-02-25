package com.giant.mindplates.biz.user.repository;
import com.giant.mindplates.biz.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail (String email);
    User findByActivationToken (String activationToke);
    Long countByEmailAndUseYn(String email, Boolean useYn);
}

