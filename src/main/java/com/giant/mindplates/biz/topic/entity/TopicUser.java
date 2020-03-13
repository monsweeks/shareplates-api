package com.giant.mindplates.biz.topic.entity;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "topic_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "topic_id"})})
public class TopicUser extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
