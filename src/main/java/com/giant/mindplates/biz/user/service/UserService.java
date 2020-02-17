package com.giant.mindplates.biz.user.service;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        userRepository.saveAndFlush(user);
        user.setLastUpdatedBy(user.getId());
        user.setCreatedBy(user.getId());
        return userRepository.save(user);
    }

    public User get(long id) {
        return userRepository.findById(id).get();
    }



    public void delete(long id) {
        userRepository.deleteById(id);
    }

}
