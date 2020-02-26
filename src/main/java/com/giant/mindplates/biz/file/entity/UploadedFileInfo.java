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
    @Length(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Length(min = 2, max = 200)
    @Column(name = "file_name", nullable = false)
    private String file_name;

    @NotBlank
    @Column(name = "file_size", nullable = false)
    private String file_size;
    
    @NotBlank
    @Column(name = "file_type", nullable = false)
    private String file_type;
    
}
