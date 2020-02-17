package com.giant.mindplates.biz.user.controller;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("")
    public User create(@Valid @RequestBody User user) {
        Date now = new Date();
        user.setActivateYn(false);
        user.setDeleteYn(false);
        user.setUseYn(true);
        user.setCreationDate(now);
        user.setLastUpdateDate(now);
        userService.save(user);
        return user;
    }

    @GetMapping("")
    public List<User> list() {
        return userService.listAll();
    }

}
