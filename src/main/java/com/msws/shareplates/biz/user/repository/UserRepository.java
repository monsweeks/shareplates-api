package com.msws.shareplates.biz.user.repository;

import com.msws.shareplates.biz.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByActivationToken(String activationToke);

    Long countByEmailAndUseYn(String email, Boolean useYn);

    @Query("SELECT new User(u.id, u.email, u.name, u.info, u.dateTimeFormat, u.language) FROM GrpUser ou INNER JOIN ou.grp o INNER JOIN ou.user u WHERE o.useYn = 1 AND u.useYn = 1 AND u.allowSearchYn = 1 AND o.id = :grpId AND (u.name LIKE CONCAT('%' ,:condition) OR u.email LIKE CONCAT('%' ,:condition) )")
    List<User> selectByGrp(@Param("grpId") Long grpId, @Param("condition") String condition);

    @Query("SELECT new User(u.id, u.email, u.name, u.info, u.dateTimeFormat, u.language) FROM User u WHERE u.useYn = 1 AND u.allowSearchYn = 1 AND (u.name LIKE CONCAT(:condition, '%') OR u.email LIKE CONCAT(:condition, '%') )")
    List<User> selectByName(@Param("condition") String condition);

    @Query("SELECT new User(u.id, u.email, u.name, u.info, u.dateTimeFormat, u.language) FROM User u WHERE u.id in (SELECT tu.user.id FROM TopicUser tu WHERE tu.topic.id = :topicId)")
    List<User> selectTopicUserList(@Param("topicId") Long topicId);
}

