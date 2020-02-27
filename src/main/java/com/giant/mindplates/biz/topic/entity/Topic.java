package com.giant.mindplates.biz.topic.entity;

import com.giant.mindplates.biz.chapter.entity.Chapter;
import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Builder
@Table(name = "topic")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Topic extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "text", name = "summary")
    private String summary;

    @NotBlank
    @Column(name = "use_yn")
    private Boolean useYn;

    @ManyToOne
    @JoinColumn(name="owner_user_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name="organization_id")
    private Organization organization;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Chapter> chapters;
}
