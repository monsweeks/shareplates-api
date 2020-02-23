package com.giant.mindplates.common.data.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
//TODO Session 객체 생성 시 Auditor구현 필요
public class CommonEntity {

    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;


}
