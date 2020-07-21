package com.msws.shareplates.biz.share.service;

import com.msws.shareplates.biz.share.entity.*;
import com.msws.shareplates.biz.share.repository.*;
import com.msws.shareplates.biz.share.vo.request.ShareSearchConditions;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.code.ChatTypeCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ShareTimeBucketRepository shareTimeBucketRepository;

    public Share createShare(Share share, Long userId) {
        share.setOpenYn(true);
        share.setStartedYn(false);
        share.setAdminUser(User.builder().id(userId).build());

        ShareTimeBucket shareTimeBucket = ShareTimeBucket.builder().share(share).openDate(LocalDateTime.now()).build();
        shareTimeBucket.setShare(share);
        share.getShareTimeBuckets().add(shareTimeBucket);

        AccessCode accessCode = accessCodeService.selectAccessCodeByCode(share.getAccessCode());
        accessCode.setShare(share);
        accessCodeService.updateAccessCode(accessCode);

        return shareRepository.save(share);
    }

    public Share updateShareStart(Share share, Long userId) {
        share.setStartedYn(false);
        share.setOpenYn(true);
        share.setAdminUser(User.builder().id(userId).build());

        ShareTimeBucket shareTimeBucket = ShareTimeBucket.builder().share(share).openDate(LocalDateTime.now()).build();
        shareTimeBucket.setShare(share);
        share.getShareTimeBuckets().add(shareTimeBucket);

        return shareRepository.save(share);
    }

    public Share updateShare(Share share) {
        return shareRepository.save(share);
    }

    public Share updateShareStop(Share share) {
        share.setStartedYn(false);
        share.setOpenYn(false);
        if (share.getShareTimeBuckets().size() > 0) {
            share.getShareTimeBuckets().get(share.getShareTimeBuckets().size() - 1).setCloseDate(LocalDateTime.now());
        }

        return shareRepository.save(share);
    }

    public List<Share> selectShareListByTopicId(Long topicId) {
        // return shareRepository.selectShareListByTopicId(topicId);
        return shareRepository.findAllByTopicId(topicId);
    }

    public Share selectShare(long shareId) {
        return shareRepository.findById(shareId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.SHARE_NOT_EXISTS_SHARE));
    }

    public Share selectShare(String accessCode) {
        return shareRepository.findByAccessCode(accessCode).orElseThrow(() -> new ServiceException(ServiceExceptionCode.SHARE_NOT_EXISTS_SHARE));
    }

    public Share selectShare(long shareId, String accessCode) {
        return shareRepository.findByIdAndAccessCode(shareId, accessCode).orElseThrow(() -> new ServiceException(ServiceExceptionCode.SHARE_NOT_EXISTS_SHARE));
    }

    public Share selectShareInfo(long shareId) {
        return shareRepository.selectShareInfo(shareId);
    }

    public void deleteShare(Share share) {
        accessCodeService.deleteAccessCodeByShareId(share.getId());
        shareRepository.delete(share);
    }

    public List<Share> selectOpenShareList(Long userId, ShareSearchConditions conditions) {
        return shareRepository.findAllByOpenYnTrueAndPrivateYnFalseAndNameContainingIgnoreCaseOrOpenYnTrueAndPrivateYnTrueAndAdminUserIdAndNameContainingIgnoreCase(conditions.getSearchWord(), userId, conditions.getSearchWord(), conditions.getDirection().equals("asc") ? Sort.by(conditions.getOrder()).ascending() : Sort.by(conditions.getOrder()).descending());
    }

    public Long selectOpenShareCount(Long userId) {
        return shareRepository.selectOpenShareCount(userId);
    }

    public boolean createOrUpdateShareUser(ShareUser shareUser) {
        ShareUser sUser = shareUserRepository.findByShareIdAndUserId(shareUser.getShare().getId(), shareUser.getUser().getId());
        if (sUser == null) {
            shareUser.setStatus(SocketStatusCode.ONLINE);
            shareUserRepository.save(shareUser);
            return true;
        } else {
            sUser.setStatus(SocketStatusCode.ONLINE);
            sUser.setFocusYn(shareUser.getFocusYn());
            shareUserRepository.save(sUser);
            return false;
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

    public void deleteShareUserSocket(long shareId, long userId) {
        shareUserSocketRepository.deleteShareUserSocket(shareId, userId);
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

    public void deleteShareUserById(ShareUser shareUser) {
        shareUserRepository.deleteShareUserById(shareUser.getId());
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

    public void updateFocusById(Long id, Boolean focus) {
        shareUserRepository.updateFocusById(id, focus);
    }


    public ShareUser updateShareUserBan(long shareId, long userId) {
        ShareUser shareUser = this.selectShareUser(shareId, userId);
        shareUser.setBanYn(true);
        shareUser.setStatus(SocketStatusCode.OFFLINE);
        shareUser.setFocusYn(false);
        this.deleteShareUserSocket(shareId, userId);
        this.updateShareUser(shareUser);

        return shareUser;
    }

    public ShareUser updateShareUserAllow(long shareId, long userId) {
        ShareUser shareUser = this.selectShareUser(shareId, userId);
        shareUser.setBanYn(false);
        shareUser.setStatus(SocketStatusCode.OFFLINE);
        this.updateShareUser(shareUser);

        return shareUser;
    }

    public ShareUser updateShareUserKickOut(long shareId, long userId) {
        ShareUser shareUser = this.selectShareUser(shareId, userId);
        this.deleteShareUserSocket(shareId, userId);
        this.deleteShareUserById(shareUser);

        return shareUser;
    }

    public Boolean selectIsBanUser(long shareId, long userId) {
        return shareUserRepository.countByShareUserBan(shareId, userId) > 0L;
    }

    public void deleteAllUserShareInfo(Long userId) {
        shareUserRepository.deleteAllShareUserByUserId(userId);
        chatRepository.deleteAllByUserId(userId);
    }

    public List<Chat> selectShareChatList(Long shareId) {
        return chatRepository.findAllByShareIdOrderByCreationDateAsc(shareId);
    }

    public Long selectFocusedSocketCount(Long shareId, Long userId) {
        return shareUserSocketRepository.countByShareUserShareIdAndShareUserUserIdAndFocusYnTrue(shareId, userId);
    }

    public Long selectShareAdminUserId(Long shareId) {
        return shareRepository.selectShareAdminUserId(shareId);
    }

    public Long selectShareTopicId(Long shareId) {
        return shareRepository.selectShareTopicId(shareId);
    }

}
