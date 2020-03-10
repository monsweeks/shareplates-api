package com.giant.mindplates.biz.organization.entity;

import com.giant.mindplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Organization extends CommonEntity {

    public Organization (Long id, String name, Boolean publicYn) {
        this.id = id;
        this.name = name;
        this.publicYn = publicYn;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organization", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    List<OrganizationUser> users = new ArrayList<>();
}
