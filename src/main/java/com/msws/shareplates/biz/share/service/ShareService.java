package com.msws.shareplates.biz.share.service;

import com.msws.shareplates.biz.share.entity.*;
import com.msws.shareplates.biz.share.repository.ChatRepository;
import com.msws.shareplates.biz.share.repository.ShareRepository;
import com.msws.shareplates.biz.share.repository.ShareUserRepository;
import com.msws.shareplates.biz.share.repository.ShareUserSocketRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.ChatTypeCode;
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

    @Autowired
    private ShareUserSocketRepository shareUserSocketRepository;

    @Autowired
    private ChatRepository chatRepository;

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


    public ShareUser createOrUpdateShareUser(ShareUser shareUser) {
        ShareUser sUser = shareUserRepository.findByShareIdAndUserId(shareUser.getShare().getId(), shareUser.getUser().getId());
        if (sUser == null) {
            shareUser.setStatus(SocketStatusCode.ONLINE);
            shareUserRepository.save(shareUser);
            return shareUser;
        } else {
            sUser.setStatus(SocketStatusCode.ONLINE);
            shareUserRepository.save(sUser);
            return sUser;
        }
    }


    public ShareUserSocket createShareUserSocket(ShareUserSocket shareUserSocket) {
        return shareUserSocketRepository.save(shareUserSocket);
    }

    public ShareUserSocket updateShareUserSocket(ShareUserSocket shareUserSocket) {
        return shareUserSocketRepository.save(shareUserSocket);
    }

    public Long countSessionByShareIdAndUserId(long shareId, long userId) {
        return shareUserSocketRepository.countSessionByShareIdAndUserId(shareId, userId);
    }

    public ShareUserSocket selectShareUserSocket(String sessionId) {
        return shareUserSocketRepository.findBySessionId(sessionId);
    }


    public void deleteShareUserSocket(String sessionId) {
        shareUserSocketRepository.deleteBySessionId(sessionId);
    }

    public ShareUser selectShareUser(long shareId, long userId) {
        return shareUserRepository.findByShareIdAndUserId(shareId, userId);
    }

    public ShareUser updateShareUser(ShareUser shareUser) {
        return shareUserRepository.save(shareUser);
    }

    public List<ShareUser> selectShareUserList(long shareId) {
        return shareUserRepository.findAllByShareId(shareId);
    }

    public Chat selectLastReadyChat(long shareId, long userId) {
        return chatRepository.findFirstByTypeAndShareIdAndUserIdOrderByCreationDateDesc(ChatTypeCode.READY, shareId, userId).orElse(Chat.builder().build());
    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public void updateStatusById(Long id, SocketStatusCode code) {
        shareUserRepository.updateStatusById(id, code);
    }

}
