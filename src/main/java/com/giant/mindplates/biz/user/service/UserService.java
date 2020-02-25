package com.giant.mindplates.biz.user.service;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.repository.UserRepository;
import com.giant.mindplates.util.EncryptUtil;
import com.giant.mindplates.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    public User selectUser(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByActivationToken(String token, Boolean activationYn) {
        return userRepository.findByActivationTokenAndActivateYn(token, activationYn);
    }

    public User updateUserActivationYn(User user, Boolean activationYn) {
        user.setActivateYn(activationYn);
        return userRepository.save(user);
    }

    public User login(String email, String password) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email);

        String salt = user.getSalt();
        byte[] saltBytes = new java.math.BigInteger(salt, 16).toByteArray();
        String encryptedText = encryptUtil.getEncrypt(password, saltBytes);

        if (user.getPassword().equals(encryptedText)) {
            return user;
        } else {
            return null;
        }
    }

    public User updateUserActivateMailSendResult(User user, Boolean result) {
        user.setActivateMailSendResult(result);
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
