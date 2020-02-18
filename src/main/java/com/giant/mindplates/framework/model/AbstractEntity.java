package com.giant.mindplates.framework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;


}
