package com.msws.shareplates.biz.grp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msws.shareplates.biz.grp.entity.Grp;
import com.msws.shareplates.biz.grp.entity.GrpUser;
import com.msws.shareplates.biz.grp.repository.GrpRepository;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.AuthCode;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;

@Service
@Transactional
public class GrpService {

    @Autowired
    private GrpRepository grpRepository;

    @Autowired
    private TopicRepository topicRepository;

    public void checkGrpIncludesUser(Long grpId, Long userId) {
        Grp grp = selectGrp(grpId);

        if (grp == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        if (grp.getPublicYn()) {
            return;
        }

        boolean isIncludeUser = grp.getUsers().stream().filter(grpUser -> grpUser.getUser().getId().equals(userId)).count() > 0;
        if (isIncludeUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }

    public void checkIsUserGrpAdmin(Long grpId, Long userId) {
        Grp grp = selectGrp(grpId);

        if (grp == null) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        if (grp.getPublicYn()) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND);
        }

        boolean isAdminUser = grp.getUsers().stream().filter(grpUser -> grpUser.getUser().getId().equals(userId) && grpUser.getRole() == RoleCode.ADMIN).count() > 0;
        if (isAdminUser) {
            return;
        }

        throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
    }

    public Grp selectGrp(long id) {
        return grpRepository.findById(id).orElse(null);
    }

    public List<Grp> selectUserGrpList(Long userId, Boolean includePublic) {
        List<Grp> grps = new ArrayList<>();
        if (includePublic) {
            grps.addAll(grpRepository.findPublicGrp());
        }
        grps.addAll(grpRepository.findUserGrp(true, userId));
        return grps;
    }

    public List<Grp> selectGrpListByUser(Long userId, String searchWord, String order, String direction) {
        return grpRepository.findGrpListByUser(userId, searchWord, true, direction.equals("asc") ? Sort.by(order).ascending() : Sort.by(order).descending());
    }

    public List<Grp> selectPublicGrpList() {
        return grpRepository.findPublicGrp();
    }
    
    @CacheEvict(value="groupCache", allEntries = true)
    public Grp createGrp(Grp grp) {
        return grpRepository.save(grp);
    }
    
    @CacheEvict(value="groupCache", allEntries = true)
    public void updateGrp(Grp grpInfo) {

        Grp grp = grpRepository.findById(grpInfo.getId()).orElse(null);

        HashMap<Long, RoleCode> nextUserMap = new HashMap<>();
        for (GrpUser user : grpInfo.getUsers()) {
            nextUserMap.put(user.getUser().getId(), user.getRole());
        }

        grp.setName(grpInfo.getName());
        grp.setDescription(grpInfo.getDescription());

        // UPDATE AND REMOVE
        HashMap<Long, Boolean> currentUserMap = new HashMap<>();
        List<GrpUser> users = grp.getUsers();
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            GrpUser user = (GrpUser) iterator.next();
            if (nextUserMap.containsKey(user.getUser().getId())) {
                user.setRole(nextUserMap.get(user.getUser().getId()));
                currentUserMap.put(user.getUser().getId(), true);
            } else {
                iterator.remove();
            }
        }

        // INSERT
        for (Long userId : nextUserMap.keySet()) {
            if (!currentUserMap.containsKey(userId)) {
                users.add(GrpUser.builder().user(User.builder().id(userId).build()).grp(grp).role(nextUserMap.get(userId)).build());
            }
        }

        if (users.size() < 1 || users.stream().filter(user -> user.getRole().equals(RoleCode.ADMIN.getCode())).count() < 1) {
            throw new ServiceException(ServiceExceptionCode.NO_MANAGER_ASSIGNED);
        }

        grpRepository.save(grp);
    }

    @CacheEvict(value="groupCache", allEntries = true)
    public void deleteGrp(Long grpId) {
        Grp grp = selectGrp(grpId);
        Long count = topicRepository.countByGrpId(grpId);
        if (count > 0) {
            throw new ServiceException(ServiceExceptionCode.NO_EMPTY_ORGANIZATION);
        }
        grp.setUseYn(false);
        grpRepository.save(grp);
    }

    public Long selectPublicGrpCount() {
        return grpRepository.countByUseYnTrueAndPublicYnTrue();
    }

    public AuthCode selectUserGrpRole(Long grpId, Long userId) {
        Boolean isPublicGrp = grpRepository.isPublicGrp(grpId);
        if (isPublicGrp) {
            return AuthCode.WRITE;
        } else {
            String role = grpRepository.findUserGrpRole(grpId, userId);
            
            if (RoleCode.ADMIN.getCode().equals(role)) {
                return AuthCode.WRITE;
            } else if (RoleCode.MEMBER.getCode().equals(role)) {
                return AuthCode.READ;
            }
            return AuthCode.NONE;
        }
    }

}
