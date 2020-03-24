package com.msws.shareplates.biz.user.controller;

import com.msws.shareplates.biz.organization.entity.Organization;
import com.msws.shareplates.biz.organization.service.OrganizationService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.common.mail.MailService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.framework.annotation.AdminOnly;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.exception.BizException;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public User create(@Valid @RequestBody User user) {

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

    @GetMapping("")
    public List<User> search(@RequestParam Long organizationId, @RequestParam String condition) {
        return userService.selectUserList(organizationId, condition);
    }

    @DisableLogin
    @GetMapping("/my-info")
    public Map my(UserInfo userInfo) {
        Map<String, Object> info = new HashMap<>();

        if (userInfo == null) {
            info.put("user", null);
            info.put("organizations", organizationService.selectPublicOrganizationList());
        } else {
            User user = userService.selectUser(userInfo.getId());
            info.put("user", user);
            List<Organization> organizations = organizationService.selectUserOrganizationList(userInfo.getId(), true);
            info.put("organizations", organizations);
        }


        return info;
    }

    @PutMapping("/my-info")
    public Map updateMyInfo(@Validated(User.ValidationUpdate.class) @RequestBody User user, HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        Long userId = SessionUtil.getUserId(request);
        if (!user.getId().equals(userId)) {
            throw new BizException(messageSourceAccessor.getMessage("common.error.badRequest"));
        }

        userService.updateUser(user);
        List<Organization> organizations = organizationService.selectUserOrganizationList(userId, true);

        info.put("user", user);
        info.put("organizations", organizations);

        return info;
    }

    @DisableLogin
    @GetMapping("/activations")
    public User getUserByActivationToken(@RequestParam String token) {

        User user = userService.getUserByActivationToken(token);
        if (user == null) {
            throw new BizException(messageSourceAccessor.getMessage("common.error.badRequest"));
        }

        return user;
    }

    @DisableLogin
    @PutMapping("/activations")
    public User setUserActivation(@RequestParam String token) {
        User user = this.getUserByActivationToken(token);
        if (user == null) {
            throw new BizException(messageSourceAccessor.getMessage("common.error.badRequest"));
        }

        if (user.getActivateYn()) {
            throw new BizException(messageSourceAccessor.getMessage("error.alreadyActivatedUser"));
        }
        return userService.updateUserActivationYn(user, true);
    }

    @AdminOnly
    @GetMapping("/all")
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
