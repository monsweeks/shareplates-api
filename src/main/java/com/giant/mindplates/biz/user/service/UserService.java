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

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setActivateYn(false);
        user.setDeleteYn(false);
        user.setUseYn(true);
        user.setCreationDate(now);
        user.setLastUpdateDate(now);

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        String tokenString = uuidString.replaceAll("-", "");
        user.setActivationToken(tokenString);

        userRepository.saveAndFlush(user);
        user.setLastUpdatedBy(user.getId());
        user.setCreatedBy(user.getId());
        return userRepository.save(user);
    }

    public User get(long id) {
        return userRepository.findById(id).get();
    }

    public User get(String email) {
        return userRepository.findByEmail(email);
    }


    public void delete(long id) {
        userRepository.deleteById(id);
    }

}
