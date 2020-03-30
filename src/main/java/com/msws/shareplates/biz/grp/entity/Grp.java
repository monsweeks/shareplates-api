package com.msws.shareplates.biz.grp.entity;

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

import com.msws.shareplates.biz.grp.vo.request.GrpRequest;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "grp")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Grp extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "grp", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT)
    List<GrpUser> users = new ArrayList<>();

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

    public Grp(Long id, String name, Boolean publicYn) {
        this.id = id;
        this.name = name;
        this.publicYn = publicYn;
    }

    public Grp(Long id, String name, String description, Boolean publicYn, LocalDateTime creationDate, Long userCount, Long topicCount, AuthCode role) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.publicYn = publicYn;
        this.setCreationDate(creationDate);
        this.userCount = userCount;
        this.topicCount = topicCount;
        this.role = role;
    }

    public Grp(GrpRequest grp) {
        this.id = grp.getId();
        this.name = grp.getName();
        this.description = grp.getDescription();
        this.publicYn = false;
        this.useYn = true;
        this.userCount = grp.getUserCount();
        this.topicCount = grp.getTopicCount();
        this.role = grp.getRole();

        this.users = Stream.concat(grp.getAdmins().stream().map(user
                        -> GrpUser.builder()
                        .user(User.builder().id(user.getId()).build())
                        .grp(this)
                        .role(AuthCode.ADMIN).build()),

                grp.getMembers().stream().map(user
                        -> GrpUser.builder()
                        .user(User.builder().id(user.getId()).build())
                        .grp(this)
                        .role(AuthCode.MEMBER).build())
        ).collect(Collectors.toList());
    }
}
