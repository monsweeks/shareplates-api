package com.giant.mindplates.biz.user.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.repository.UserRepository;
import com.giant.mindplates.common.exception.ServiceException;
import com.giant.mindplates.common.exception.code.ServiceExceptionCode;
import com.giant.mindplates.util.EncryptUtil;

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
    public Boolean selectsExistEmail(String email) {
        if (userRepository.countByEmailAndUseYn(email, true) > 0L) {
            return true;
        }

        return false;
    }

    public User createUser(User user) throws NoSuchAlgorithmException {
        LocalDateTime now = LocalDateTime.now();
        user.setActivateYn(false);
        user.setDeleteYn(false);
        user.setUseYn(true);
        user.setCreationDate(now);
        user.setLastUpdateDate(now);

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

    public User selcetUser(long id) {
        return userRepository.findById(id).get();
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
