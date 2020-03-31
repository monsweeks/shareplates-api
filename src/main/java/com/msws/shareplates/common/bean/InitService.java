package com.msws.shareplates.common.bean;

import java.time.LocalDateTime;

import com.msws.shareplates.biz.grp.entity.Grp;
import org.springframework.transaction.annotation.Transactional;

import com.msws.shareplates.biz.grp.service.GrpService;

import lombok.extern.java.Log;

@Log
@Transactional
public class InitService {

    private GrpService grpService;

    public InitService (GrpService grpService) {
        this.grpService = grpService;
    }

    public void init() {
        Long count = grpService.selectPublicGrpCount();
        if (count < 1) {
            LocalDateTime now = LocalDateTime.now();
            Grp publicGrp = Grp.builder().name("default").useYn(true).publicYn(true).build();
            publicGrp.setCreationDate(now);
            publicGrp.setLastUpdateDate(now);
            grpService.createGrp(publicGrp);
        }
    }
}
