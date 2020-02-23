package com.giant.mindplates.biz.user.controller;

import com.giant.mindplates.biz.common.service.MailService;
import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @PostMapping("")
    public User create(@Valid @RequestBody User user) throws Exception {

        User existUser = userService.get(user.getEmail());

        if (existUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.");
        }

        userService.create(user);
        // TODO 실패했을 경우, 실패 내역을 입력하고, 재발송이나, 어드민 알림 등의 기능을 추가해야 함
        mailService.sendUserActivationMail(user.getEmail(), user.getActivationToken());

        return user;
    }

    @GetMapping("/exists")
    public Boolean existEmail(@RequestParam String email) {
        User existUser = userService.get(email);
        return existUser != null;
    }

    @GetMapping("")
    public List<User> list() {
        return userService.listAll();
    }

}
