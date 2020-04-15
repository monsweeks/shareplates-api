package com.msws.shareplates.biz.share.service;

import com.msws.shareplates.biz.share.entity.AccessCode;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.repository.ShareRepository;
import com.msws.shareplates.biz.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private AccessCodeService accessCodeService;

    public Share createShare(Share share, Long userId) {
        share.setOpenYn(true);
        share.setAdminUser(User.builder().id(userId).build());
        share.setLastOpenDate(LocalDateTime.now());

        AccessCode accessCode = accessCodeService.selectAccessCodeByCode(share.getAccessCode());
        accessCode.setShare(share);

        accessCodeService.updateAccessCode(accessCode);

        return shareRepository.save(share);
    }

    public Share updateShareStart(Share share, Long userId) {
        share.setOpenYn(true);
        share.setAdminUser(User.builder().id(userId).build());
        share.setLastOpenDate(LocalDateTime.now());
        return shareRepository.save(share);
    }

    public Share updateShareStop(Share share) {
        share.setOpenYn(false);
        share.setLastCloseDate(LocalDateTime.now());
        return shareRepository.save(share);
    }

    public List<Share> selectShareListByTopicId(Long topicId, Long userId) {
        return shareRepository.selectShareListByTopicId(topicId, userId);
    }

    public Share selectShare(long shareId) {
        return shareRepository.findById(shareId).orElse(null);
    }

    public void deleteShare(Share share) {
        accessCodeService.deleteAccessCodeByShareId(share.getId());
        shareRepository.delete(share);
    }

    public List<Share> selectOpenShareList() {
        return shareRepository.selectOpenShareList();
    }


}
