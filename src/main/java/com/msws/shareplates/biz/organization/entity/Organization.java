package com.msws.shareplates.biz.organization.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import com.msws.shareplates.biz.organization.vo.request.OrganizationRequest;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Organization extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT)
    List<OrganizationUser> users = new ArrayList<>();

    @Length(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Length(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;

    @Column(name = "public_yn", nullable = false)
    private Boolean publicYn;

    @Transient
    private Long userCount;

    @Transient
    private Long topicCount;

    @Transient
    private AuthCode role;

    public Organization(Long id, String name, Boolean publicYn) {
        this.id = id;
        this.name = name;
        this.publicYn = publicYn;
    }

    public Organization(Long id, String name, String description, Boolean publicYn, LocalDateTime creationDate, Long userCount, Long topicCount, AuthCode role) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.publicYn = publicYn;
        this.setCreationDate(creationDate);
        this.userCount = userCount;
        this.topicCount = topicCount;
        this.role = role;
    }

    public Organization(OrganizationRequest organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
        this.publicYn = false;
        this.useYn = true;
        this.userCount = organization.getUserCount();
        this.topicCount = organization.getTopicCount();
        this.role = organization.getRole();

        this.users = Stream.concat(organization.getAdmins().stream().map(user
                        -> OrganizationUser.builder()
                        .user(User.builder().id(user.getId()).build())
                        .organization(this)
                        .role(AuthCode.ADMIN).build()),

                organization.getMembers().stream().map(user
                        -> OrganizationUser.builder()
                        .user(User.builder().id(user.getId()).build())
                        .organization(this)
                        .role(AuthCode.MEMBER).build())
        ).collect(Collectors.toList());
    }
}
