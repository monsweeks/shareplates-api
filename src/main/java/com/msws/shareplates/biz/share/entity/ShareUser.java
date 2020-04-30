package com.msws.shareplates.biz.share.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "share_user")
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
    
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private SocketStatusCode status;
    
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private RoleCode role;

    @Column(name = "ban_yn")
    private Boolean banYn;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "share_user_id")
    private List<ShareUserSocket> shareUserSocketList;
}
