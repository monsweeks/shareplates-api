package com.msws.shareplates.biz.organization.entity;

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
@Table(name = "organization_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "organization_id"})})
public class OrganizationUser extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "role", nullable = false)
    private String role; // ADMIN or MEMBER 나중에 혹시 추가될 수 있어서 스트링으로

    @Column(name = "status"/*, nullable = false*/)
    private String status; // INVITE, REQUEST, APPROVE (DENY는 없고, DENY시 삭제)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
