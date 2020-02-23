package com.giant.mindplates.biz.organization.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.giant.mindplates.common.data.domain.CommonEntity;

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

    @Length(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;

    @Column(name = "delete_yn", nullable = false)
    private Boolean deleteYn;

    @Column(name = "picture_path")
    private String picturePath;
}
