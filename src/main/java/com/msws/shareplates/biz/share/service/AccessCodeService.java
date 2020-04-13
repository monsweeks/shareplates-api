package com.msws.shareplates.biz.share.service;

import com.msws.shareplates.biz.share.entity.AccessCode;
import com.msws.shareplates.biz.share.repository.AccessCodeRepository;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;


@Service
@Transactional
public class AccessCodeService {

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    private String getRandomNumber() throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);
        int min = 100000;
        int code = min + secureRandomGenerator.nextInt(899999);
        Long count = accessCodeRepository.countByAccessCode(Integer.toString(code));

        while (count > 0L) {
            code = min + secureRandomGenerator.nextInt(899999);
            count = accessCodeRepository.countByAccessCode(Integer.toString(code));
        }

        return Integer.toString(code);
    }

    public AccessCode createAccessCode(Long userId) throws NoSuchProviderException, NoSuchAlgorithmException {
        String code = this.getRandomNumber();
        AccessCode accessCode = AccessCode.builder().code(code).user(User.builder().id(userId).build()).build();
        return accessCodeRepository.save(accessCode);
    }

    public AccessCode updateAccessCode(Long accessCodeId, Long userId) throws NoSuchProviderException, NoSuchAlgorithmException {
        AccessCode accessCode = accessCodeRepository.findByIdAndUserId(accessCodeId, userId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.RESOURCE_NOT_FOUND));
        String code = this.getRandomNumber();
        accessCode.setCode(code);
        return accessCodeRepository.save(accessCode);
    }

    public AccessCode selectAccessCode(Long accessCodeId) {
        return accessCodeRepository.findById(accessCodeId).orElse(null);
    }

    public AccessCode selectAccessCode(Long accessCodeId, Long userId) {
        return accessCodeRepository.findByIdAndUserId(accessCodeId, userId).orElse(null);
    }

    public void deleteAccessCode(long accessCodeId) {
        AccessCode accessCode = accessCodeRepository.findById(accessCodeId).orElse(null);
        accessCodeRepository.delete(accessCode);
    }


}
