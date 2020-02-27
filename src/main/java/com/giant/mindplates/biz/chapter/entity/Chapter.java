package com.giant.mindplates.biz.chapter.entity;

import com.giant.mindplates.biz.page.entity.Page;
import com.giant.mindplates.biz.topic.entity.Topic;
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
@Table(name = "chapter")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chapter extends CommonEntity {

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

    @Column(name = "order_no")
    Integer orderNo;

    @NotBlank
    @Column(name = "use_yn")
    private Boolean useYn;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chapter", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Page> pages;
}
