package com.msws.shareplates.biz.share.entity;

import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.data.domain.CommonEntity;
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
@Table(name = "share_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"share_id", "user_id"})})
public class ShareUser extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_id")
    private Share share;
    
    @Column(name="uuid")
    private String uuid;
}
