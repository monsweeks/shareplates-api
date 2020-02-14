package com.giant.mindplates.biz.organization.entity;

import com.giant.mindplates.framework.model.AbstractEntity;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "organization")
@Data
public class Organization extends AbstractEntity {

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
