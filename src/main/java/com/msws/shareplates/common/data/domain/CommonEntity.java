package com.msws.shareplates.common.data.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
public class CommonEntity {

	@CreatedDate
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;
	
	@CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    
    @LastModifiedDate
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    
    @LastModifiedBy
    @Column(name = "last_updated_by")
    private Long lastUpdatedBy;


}
