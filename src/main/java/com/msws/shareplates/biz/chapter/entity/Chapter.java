package com.msws.shareplates.biz.chapter.entity;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "text", name = "summary")
    private String summary;

    @Column(name = "order_no")
    private int orderNo;

    @Column(name = "use_yn")
    private Boolean useYn;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "page_count")
    private Integer pageCount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chapter", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Page> pages;

    @Column(columnDefinition = "text", name = "content")
    private String content;
    
    public Chapter setTitle(String title) {
    	this.title = title;
    	
    	return this;
    }
}
