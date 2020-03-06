package com.giant.mindplates.biz.topic.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "use_yn")
    private Boolean useYn;
    
    @Column(name = "organization_id")
    private long organizationId;
    
    @Column(name = "icon_index")
    private int iconIndex;

    @ManyToOne
    @JoinColumn(name="owner_user_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name="organization_id", insertable = false, updatable = false)
    private Organization organization;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL)
    private List<TopicUser> topicUser;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @Fetch(value = FetchMode.SUBSELECT)
//    private List<Chapter> chapters;
}
