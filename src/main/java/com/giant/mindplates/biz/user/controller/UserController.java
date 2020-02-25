package com.giant.mindplates.biz.user.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.service.UserService;
import com.giant.mindplates.common.mail.MailService;
import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.util.SessionUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    MessageSourceAccessor messageSourceAccessor;

    @PostMapping("")
    public User create(@Valid @RequestBody User user) throws Exception {

        userService.selectUserByEmail(user.getEmail());

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
        return userService.checkEmail(email);
    }

    @GetMapping("/activations")
    public User getUserByActivationToken(@RequestParam String token) {
        return userService.getUserByActivationToken(token, false);
    }

    @PutMapping("/activations")
    public User setUserActivation(@RequestParam String token) {
        User user = userService.getUserByActivationToken(token,false);
        
        return userService.updateUserActivationYn(user, true);
    }

    @GetMapping("")
    public List<User> list() {
        return userService.selectUserList();
    }

    @DisableLogin
    @PostMapping("/login")
    public Boolean login(@RequestParam String email, @RequestParam String password,  HttpServletRequest request) throws NoSuchAlgorithmException {

        User user = userService.login(email, password);
        if (user != null) {
            sessionUtil.login(request, user.getId());
            return true;
        }

        return false;
    }

    @DisableLogin
    @GetMapping("/logout")
    public void logout( HttpServletRequest request) {
        sessionUtil.logout(request);
    }

}
