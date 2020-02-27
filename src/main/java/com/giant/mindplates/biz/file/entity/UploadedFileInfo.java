package com.giant.mindplates.biz.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.giant.mindplates.common.data.domain.CommonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "UploadFile")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadedFileInfo extends CommonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    
    @NotBlank
    @Column(name = "owner", nullable = false)
    private String owner;
    
    @NotBlank
    @Length(min = 2, max = 400)
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "use_yn", nullable = false)
    private Boolean useYn;
    
    @Column(name = "share_yn", nullable = false)
    private Boolean shareYn;

    
}
