package com.giant.mindplates.biz.user.repository;
import com.giant.mindplates.biz.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
