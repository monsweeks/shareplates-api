package com.msws.shareplates.biz.page.entity;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.common.data.domain.CommonEntity;
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
    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "text", name = "content")
    private String content;

    @Column(name = "order_no")
    private int orderNo;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public Page setTitle(String title) {
        this.title = title;

        return this;
    }
}
