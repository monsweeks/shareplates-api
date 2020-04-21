package com.msws.shareplates.biz.share.service;

import com.msws.shareplates.biz.share.entity.AccessCode;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.repository.ShareRepository;
import com.msws.shareplates.biz.share.repository.ShareUserRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.SocketStatusCode;
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

    @Autowired
    private ShareUserRepository shareUserRepository;

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
        share.setStartedYn(false);
        share.setOpenYn(true);
        share.setAdminUser(User.builder().id(userId).build());
        share.setLastOpenDate(LocalDateTime.now());
        return shareRepository.save(share);
    }

    public Share updateShare(Share share) {
        return shareRepository.save(share);
    }

    public Share updateShareStop(Share share) {
        share.setStartedYn(false);
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

    public Share selectShareInfo(long shareId) {
        return shareRepository.selectShareInfo(shareId);
    }

    public void deleteShare(Share share) {
        accessCodeService.deleteAccessCodeByShareId(share.getId());
        shareRepository.delete(share);
    }

    public List<Share> selectOpenShareList(Long userId) {
        return shareRepository.selectOpenShareList(userId);
    }

    public ShareUser createOrUpdateShareUserRepository(ShareUser shareUser) {
        ShareUser sUser = shareUserRepository.findByShareIdAndUserIdAndUuid(shareUser.getShare().getId(), shareUser.getUser().getId(), shareUser.getUuid());
        if (sUser == null) {
            shareUser.setStatus(SocketStatusCode.ONLINE);
            shareUserRepository.save(shareUser);
            return shareUser;
        } else {
            if (!sUser.getStatus().equals(SocketStatusCode.ONLINE)) {
                sUser.setStatus(SocketStatusCode.ONLINE);
                shareUserRepository.save(sUser);
            }

            return sUser;
        }
    }

    public ShareUser selectShareUser(long shareId, long userId, String uuid) {
        return shareUserRepository.findByShareIdAndUserIdAndUuid(shareId, userId, uuid);
    }

    public ShareUser updateShareUser(ShareUser shareUser) {
        return shareUserRepository.save(shareUser);
    }


}
