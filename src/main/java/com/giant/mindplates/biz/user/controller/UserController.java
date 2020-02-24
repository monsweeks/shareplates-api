package com.giant.mindplates.biz.user.controller;

import com.giant.mindplates.biz.common.service.MailService;
import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

        User existUser = userService.selectUser(user.getEmail());

        if (existUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록된 이메일입니다.");
        }

        userService.createUser(user);
        // TODO 실패했을 경우, 실패 내역을 입력하고, 재발송이나, 어드민 알림 등의 기능을 추가해야 함
        try {
            mailService.sendUserActivationMail(user.getEmail(), user.getActivationToken());
            userService.updateUserActivateMailSendResult(user, true);
        } catch (Exception e) {
            userService.updateUserActivateMailSendResult(user, false);
        }

        return user;
    }

    @GetMapping("/exists")
    public Boolean existEmail(@RequestParam String email) {
        User existUser = userService.selectUser(email);
        return existUser != null;
    }

    @GetMapping("/activations")
    public User getUserByActivationToken(@RequestParam String token) {
        return userService.getUserByActivationToken(token, false);
    }

    @PutMapping("/activations")
    public User setUserActivation(@RequestParam String token) {
        User user = this.getUserByActivationToken(token);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다.");
        }
        return userService.updateUserActivationYn(user, true);
    }

    @GetMapping("")
    public List<User> list() {
        return userService.selectUserList();
    }

}
