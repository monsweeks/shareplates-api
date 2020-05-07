package com.msws.shareplates.biz.file.entity;

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
@Table(name = "file_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileInfo extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "page_id")
    private Long pageId;

    @NotBlank
    @Length(min = 1, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "path", nullable = false)
    private String path;

    @NotBlank
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "size", nullable = false)
    private Long size;

}
