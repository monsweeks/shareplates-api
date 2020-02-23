package com.giant.mindplates.biz.user.service;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> selectUserList() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setActivateYn(false);
        user.setDeleteYn(false);
        user.setUseYn(true);
        user.setCreationDate(now);
        user.setLastUpdateDate(now);
        // TODO 패스워드 암호화 필요

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
        return userRepository.findByActivationTokenAAndActivateYn(token, activationYn);
    }

    public User updateUserActivationYn(User user, Boolean activationYn) {
        user.setActivateYn(activationYn);
        return userRepository.save(user);
    }

    public User updateUserActivateMailSendResult(User user, Boolean result) {
        user.setActivateMailSendResult(result);
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
