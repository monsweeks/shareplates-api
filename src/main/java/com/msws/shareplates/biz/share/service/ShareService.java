package com.msws.shareplates.biz.share.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msws.shareplates.biz.share.entity.AccessCode;
import com.msws.shareplates.biz.share.entity.Chat;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.repository.ChatRepository;
import com.msws.shareplates.biz.share.repository.ShareRepository;
import com.msws.shareplates.biz.share.repository.ShareUserRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.ChatTypeCode;
import com.msws.shareplates.common.code.SocketStatusCode;

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
    
    public Optional<ShareUser> selectShareUserByUuid(String uuid) {
    	return shareUserRepository.findByUuid(uuid);
    }

    public void deleteShare(Share share) {
        accessCodeService.deleteAccessCodeByShareId(share.getId());
        shareRepository.delete(share);
    }

    public List<Share> selectOpenShareList(Long userId) {
        return shareRepository.selectOpenShareList(userId);
    }

    public ShareUser createOrUpdateShareUserRepository(ShareUser shareUser) {
        ShareUser sUser = shareUserRepository.findByShareIdAndUserId(shareUser.getShare().getId(), shareUser.getUser().getId());
        if (sUser == null) {
            shareUser.setStatus(SocketStatusCode.ONLINE);
            
            shareUserRepository.save(shareUser);
            
            return shareUser;
        } else {
            sUser.setStatus(SocketStatusCode.ONLINE);
            sUser.setUuid(shareUser.getUuid());
            
            shareUserRepository.save(sUser);

            return sUser;
        }
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
    
    public void updateStatusByUudi(SocketStatusCode code, String uuid) {
    	shareUserRepository.updateStatusByUudi(code, uuid);
    }

}
