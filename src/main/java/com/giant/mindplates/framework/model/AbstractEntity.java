package com.giant.mindplates.framework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    @Column(name = "creation_date")
    private String creationDate;
    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "last_update_date")
    private String lastUpdateDate;
    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;


}
