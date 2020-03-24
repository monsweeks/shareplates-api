package com.msws.shareplates.biz.user.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.repository.UserRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.util.EncryptUtil;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptUtil encryptUtil;

    public List<User> selectUserList() {
        return userRepository.findAll();
    }

    public List<User> selectUserList(Long organizationId, String condition) {
        if (organizationId == null) {
            return userRepository.selectByName(condition);
        }

        return userRepository.selectByOrganization(organizationId, condition);
    }

    public List<User> selectTopicUserList(Long topicId) {
        return userRepository.selectTopicUserList(topicId);
    }

    public Boolean selectsExistEmail(String email) {
        if (userRepository.countByEmailAndUseYn(email, true) > 0L) {
            return true;
        }

        return false;
    }

    public User createUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setActivateYn(false);
        user.setUseYn(true);
        user.setCreationDate(now);
        user.setLastUpdateDate(now);
        user.setAllowSearchYn(true);

        String plainText = user.getPassword();
        byte[] saltBytes = encryptUtil.getSaltByteArray();
        String salt = encryptUtil.getSaltString(saltBytes);
        user.setSalt(salt);
        String encryptedText = encryptUtil.getEncrypt(plainText, saltBytes);
        user.setPassword(encryptedText);

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String tokenString = uuidString.replaceAll("-", "");
        user.setActivationToken(tokenString);

        userRepository.saveAndFlush(user);
        user.setLastUpdatedBy(user.getId());
        user.setCreatedBy(user.getId());
        return userRepository.save(user);
    }

    public User selectUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void selectUserByEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
        	throw new ServiceException(ServiceExceptionCode.EXIST_EMAIL);
    	});
    }
    
    public boolean checkEmail(String email) {
    	return userRepository.findByEmail(email).isPresent();
    }

    public User getUserByActivationToken(String token) {
        return userRepository.findByActivationToken(token);
    }

    public User updateUserActivationYn(User user, Boolean activationYn) {
        user.setActivateYn(activationYn);
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User userInfo = this.selectUser(user.getId());
        userInfo.setName(user.getName());
        userInfo.setDateTimeFormat(user.getDateTimeFormat());
        userInfo.setInfo(user.getInfo());
        userInfo.setLanguage(user.getLanguage());
        userInfo.setLastUpdatedBy(user.getId());
        userInfo.setLastUpdateDate(LocalDateTime.now());

        return userRepository.save(userInfo);
    }

    public User login(String email, String password) throws NoSuchAlgorithmException {
        return userRepository.findByEmail(email).filter(user -> {

            String salt = user.getSalt();
            byte[] saltBytes = new java.math.BigInteger(salt, 16).toByteArray();
            String encryptedText = encryptUtil.getEncrypt(password, saltBytes);
        	
        	return user.getPassword().equals(encryptedText);
        }).orElse(null);
    }

    public User updateUserActivateMailSendResult(User user, Boolean result) {
        user.setActivateMailSendResult(result);
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
