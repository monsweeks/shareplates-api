package com.msws.shareplates.biz.grp.controller;

import com.msws.shareplates.biz.grp.entity.Grp;
import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.grp.vo.request.GrpRequest;
import com.msws.shareplates.biz.grp.vo.response.GrpResponse;
import com.msws.shareplates.biz.grp.vo.response.GrpsResponse;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GrpController {
    @Autowired
    GrpService grpService;

    @Autowired
    SessionUtil sessionUtil;

    @GetMapping("")
    public GrpsResponse selectGrpListByUser(@RequestParam String searchWord, @RequestParam String order, @RequestParam String direction, UserInfo userInfo) {
        return new GrpsResponse(grpService.selectGrpListByUser(userInfo.getId(), searchWord, order, direction));
    }

    @GetMapping("/{grpId}")
    public GrpResponse selectGrp(@PathVariable Long grpId, UserInfo userInfo) {
        grpService.checkGrpIncludesUser(grpId, userInfo.getId());
        return new GrpResponse(grpService.selectGrp(grpId));
    }

    @PostMapping("")
    public GrpResponse createGrp(@RequestBody GrpRequest grpRequest) {
        grpService.createGrp(new Grp(grpRequest));
        Link link = new Link("/groups", "groups");
        return GrpResponse.builder().build().add(link);
    }

    @PutMapping("/{grpId}")
    public GrpResponse updateGrp(@RequestBody GrpRequest GrpRequest, UserInfo userInfo) {
        grpService.checkIsUserGrpAdmin(GrpRequest.getId(), userInfo.getId());
        grpService.updateGrp(new Grp(GrpRequest));
        Link link = new Link("/groups", "groups");
        return GrpResponse.builder().build().add(link);
    }

    @DeleteMapping("/{grpId}")
    public GrpResponse deleteGrp(@PathVariable Long grpId, UserInfo userInfo) {
        grpService.checkIsUserGrpAdmin(grpId, userInfo.getId());
        grpService.deleteGrp(grpId);
        Link link = new Link("/groups", "groups");
        return GrpResponse.builder().build().add(link);
    }

}
