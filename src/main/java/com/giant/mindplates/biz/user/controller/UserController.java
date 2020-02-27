package com.giant.mindplates.biz.user.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.giant.mindplates.biz.organization.entity.Organization;
import com.giant.mindplates.biz.organization.service.OrganizationService;
import com.giant.mindplates.biz.topic.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.giant.mindplates.biz.user.entity.User;
import com.giant.mindplates.biz.user.service.UserService;
import com.giant.mindplates.common.mail.MailService;
import com.giant.mindplates.framework.annotation.AdminOnly;
import com.giant.mindplates.framework.annotation.DisableLogin;
import com.giant.mindplates.framework.exception.BizException;
import com.giant.mindplates.util.SessionUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    MailService mailService;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    MessageSourceAccessor messageSourceAccessor;

    @DisableLogin
    @PostMapping("")
    public User create(@Valid @RequestBody User user) throws Exception {

        userService.selectUserByEmail(user.getEmail());

        userService.createUser(user);
        // TODO 실패했을 경우, 실패 내역을 입력하고, 재발송이나, 어드민 알림 등의 기능을 추가해야 함
        // TODO 메일 방송 기능을 스킵할 수 있는 시스템 옵션을 추가
        try {
            // mailService.sendUserActivationMail(user.getEmail(), user.getActivationToken());
            userService.updateUserActivateMailSendResult(user, true);
        } catch (Exception e) {
            userService.updateUserActivateMailSendResult(user, false);
        }

        return user;
    }

    @DisableLogin
    @GetMapping("/exists")
    public Boolean existEmail(@RequestParam String email) {
        return userService.checkEmail(email);
    }

    @DisableLogin
    @GetMapping("/my-info")
    public Map my(HttpServletRequest request) {
        Map<String, Object> info = new HashMap();

        Long userId = sessionUtil.getUserId(request);
        if (userId != null) {
            User user = userService.selectUser(userId);
            info.put("user", user);
        }

        List<Organization> organizations = organizationService.selectUserOrganizationList(userId);
        info.put("organizations", organizations);

        return info;
    }

    @DisableLogin
    @GetMapping("/activations")
    public User getUserByActivationToken(@RequestParam String token) {

        User user = userService.getUserByActivationToken(token);
        if (user == null) {
            throw new BizException(messageSourceAccessor.getMessage("error.badRequest"));
        }

        return user;
    }

    @DisableLogin
    @PutMapping("/activations")
    public User setUserActivation(@RequestParam String token) {
        User user = this.getUserByActivationToken(token);
        if (user == null) {
            throw new BizException(messageSourceAccessor.getMessage("error.badRequest"));
        }

        if (user.getActivateYn()) {
            throw new BizException(messageSourceAccessor.getMessage("error.alreadyActivatedUser"));
        }
        return userService.updateUserActivationYn(user, true);
    }

    @AdminOnly
    @GetMapping("")
    public List<User> list() {
        return userService.selectUserList();
    }

    @DisableLogin
    @PostMapping("/login")
    public Boolean login(@RequestBody Map<String, String> account, HttpServletRequest request) throws NoSuchAlgorithmException {

        User user = userService.login(account.get("email"), account.get("password"));
        if (user != null) {
            sessionUtil.login(request, user.getId());
            return true;
        }

        return false;
    }

    @DisableLogin
    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        sessionUtil.logout(request);
    }

}
