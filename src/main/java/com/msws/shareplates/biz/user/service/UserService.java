package com.msws.shareplates.biz.user.service;

import com.google.gson.Gson;
import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.repository.UserRepository;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptUtil encryptUtil;

    @Autowired
    private ShareService shareService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private GrpService grpService;

    public List<User> selectUserList() {
        return userRepository.findAll();
    }

    public List<User> selectUserList(Long grpId, String condition) {
        if (grpId == null) {
            return userRepository.selectByName(condition);
        }

        return userRepository.selectByGrp(grpId, condition);
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

    private String generateUserAvatar() {
        HashMap<String, Integer> data = new HashMap<>();
        data.put("bgStyleNumber", (int) Math.random() * 9);
        data.put("formNumber", (int) Math.random() * 6);
        data.put("hairNumber", (int) Math.random() * 13);
        data.put("faceNumber", (int) Math.random() * 18);
        data.put("accessoryNumber", 0);
        data.put("faceColorNumber", (int) Math.random() * 12);
        data.put("hairColorNumber", (int) Math.random() * 9);
        data.put("accessoryColorNumber", (int) Math.random() * 24);
        data.put("clothColorNumber", (int) Math.random() * 20);

        HashMap<String, Object> icon = new HashMap<>();
        icon.put("type" , "avatar");
        icon.put("data" , data);

        HashMap<String, Object> info = new HashMap<>();
        info.put("icon", icon);

        return new Gson().toJson(info);

    }

    public User createUser(User user, Boolean registerAvatar) {
        LocalDateTime now = LocalDateTime.now();
        user.setActivateYn(false);
        user.setUseYn(true);
        user.setCreationDate(now);
        user.setLastUpdateDate(now);
        user.setAllowSearchYn(true);
        user.setRoleCode(RoleCode.MEMBER);
        user.setActiveRoleCode(RoleCode.MEMBER);

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

        if (registerAvatar) {
            user.setInfo(this.generateUserAvatar());
        }

        userRepository.saveAndFlush(user);
        user.setLastUpdatedBy(user.getId());
        user.setCreatedBy(user.getId());
        return userRepository.save(user);
    }

    public User selectUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User selectUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
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

        if (user.getRegistered() != null) {
            userInfo.setRegistered(user.getRegistered());
        }
        if (user.getPassword() != null) {
            String plainText = user.getPassword();
            byte[] saltBytes = encryptUtil.getSaltByteArray();
            String salt = encryptUtil.getSaltString(saltBytes);
            user.setSalt(salt);
            String encryptedText = encryptUtil.getEncrypt(plainText, saltBytes);
            user.setPassword(encryptedText);
        }

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

    public void deleteUser(long userId) {
        grpService.deleteAllUserGrpInfo(userId);
        shareService.deleteAllUserShareInfo(userId);
        topicService.deleteAllUserTopicInfo(userId);
        userRepository.deleteById(userId);
    }

}
