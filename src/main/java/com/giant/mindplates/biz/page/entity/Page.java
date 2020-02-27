package com.giant.mindplates.biz.page.entity;

import com.giant.mindplates.biz.chapter.entity.Chapter;
import com.giant.mindplates.common.data.domain.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Builder
@Table(name = "page")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Page extends CommonEntity {

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
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
