package com.msws.shareplates.biz.user.controller;

import com.msws.shareplates.biz.file.entity.FileInfo;
import com.msws.shareplates.biz.file.service.FileInfoService;
import com.msws.shareplates.biz.file.vo.FileInfoResponse;
import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.biz.user.vo.request.UserRequest;
import com.msws.shareplates.biz.user.vo.response.MyInfoResponse;
import com.msws.shareplates.biz.user.vo.response.UserResponse;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.mail.MailService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.aop.annotation.AdminOnly;
import com.msws.shareplates.framework.exception.BizException;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    GrpService grpService;

    @Autowired
    MailService mailService;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    MessageSourceAccessor messageSourceAccessor;

    @Autowired
    private FileInfoService fileStorageService;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private ShareService shareService;

    @DisableLogin
    @PostMapping("")
    public User createUser(@Valid @RequestBody User user) {

        User storedUser = userService.selectUserByEmail(user.getEmail());
        if (storedUser != null) {
            throw new ServiceException(ServiceExceptionCode.EXIST_EMAIL);
        }

        user.setRegistered(true);
        userService.createUser(user, true);
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
    @PutMapping("/register")
    public User updateRegisterUser(@Valid @RequestBody User user, HttpServletRequest request) {

        Long userId = SessionUtil.getUserId(request);

        User storedUser = userService.selectUser(userId);

        if (storedUser == null) {
            throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER);
        }

        storedUser.setPassword(user.getPassword());
        storedUser.setName(user.getName());
        storedUser.setRegistered(true);
        userService.updateUser(storedUser);

        return user;
    }

    @DisableLogin
    @GetMapping("/exists")
    public Boolean selectUserEmailExist(@RequestParam String email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/search")
    public List<User> selectUsers(@RequestParam Long grpId, @RequestParam String condition) {
        return userService.selectUserList(grpId, condition);
    }

    @DisableLogin
    @GetMapping("/my-info")
    public MyInfoResponse selectMyInfo(UserInfo userInfo) {

        Long shareCount = shareService.selectOpenShareCount(userInfo != null ? userInfo.getId() : null);

        if (userInfo == null) {

            return MyInfoResponse.builder()
                    .shareCount(shareCount)
                    .grps(grpService.selectPublicGrpList().stream().map(group
                            -> MyInfoResponse.GroupInfo.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .publicYn(group.getPublicYn())
                            .build())
                            .collect(Collectors.toList()))
                    .build();

        } else {

            return MyInfoResponse.builder()
                    .shareCount(shareCount)
                    .user(Optional.ofNullable(userService.selectUser(userInfo.getId())).map(user
                            -> MyInfoResponse.UserInfo.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .info(user.getInfo())
                            .name(user.getName())
                            .roleCode(user.getRoleCode())
                            .activeRoleCode(user.getActiveRoleCode())
                            .language(user.getLanguage())
                            .build())
                            .orElseThrow(() -> new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER)))
                    .grps(grpService.selectUserGrpList(userInfo.getId(), true).stream().map(group
                            -> MyInfoResponse.GroupInfo.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .publicYn(group.getPublicYn())
                            .build())
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @PutMapping("/my-info")
    public MyInfoResponse updateMyInfo(@Validated(User.ValidationUpdate.class) @RequestBody User user, HttpServletRequest request) {
        Long userId = SessionUtil.getUserId(request);
        if (!user.getId().equals(userId)) {
            throw new BizException(messageSourceAccessor.getMessage("common.error.badRequest"));
        }

        User currentUser = userService.selectUser(user.getId());
        currentUser.setInfo(user.getInfo());
        currentUser.setName(user.getName());
        currentUser.setDateTimeFormat(user.getDateTimeFormat());
        currentUser.setLanguage(user.getLanguage());

        if (RoleCode.SUPER_MAN == currentUser.getRoleCode()) {
            currentUser.setRoleCode(user.getRoleCode());
            if (user.getRoleCode() == RoleCode.SUPER_MAN) {
                currentUser.setActiveRoleCode(user.getActiveRoleCode());
            } else {
                currentUser.setActiveRoleCode(RoleCode.MEMBER);
            }

            sessionUtil.login(request, user);
        }

        userService.updateUser(currentUser);

        return MyInfoResponse.builder()
                .user(Optional.ofNullable(currentUser).map(userInfo
                        -> MyInfoResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .info(user.getInfo())
                        .name(user.getName())
                        .roleCode(user.getRoleCode())
                        .activeRoleCode(user.getActiveRoleCode())
                        .language(userInfo.getLanguage())
                        .build())
                        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.UNAUTHORIZED_USER)))
                .grps(grpService.selectUserGrpList(userId, true).stream().map(group
                        -> MyInfoResponse.GroupInfo.builder()
                        .id(group.getId())
                        .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @DisableLogin
    @GetMapping("/activations")
    public User selectUserByActivationToken(@RequestParam String token) {

        User user = userService.getUserByActivationToken(token);
        if (user == null) {
            throw new BizException(messageSourceAccessor.getMessage("common.error.badRequest"));
        }

        return user;
    }

    @DisableLogin
    @PutMapping("/activations")
    public User selectUserActivation(@RequestParam String token) {
        User user = this.selectUserByActivationToken(token);
        if (user == null) {
            throw new BizException(messageSourceAccessor.getMessage("common.error.badRequest"));
        }

        if (user.getActivateYn()) {
            throw new BizException(messageSourceAccessor.getMessage("error.alreadyActivatedUser"));
        }
        return userService.updateUserActivationYn(user, true);
    }

    @AdminOnly
    @GetMapping("")
    public UserResponse selectUsers() {
        return UserResponse.builder()
                .userList(userService.selectUserList().stream().map(user -> UserResponse.User.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .info(user.getInfo())
                        .dateTimeFormat(user.getDateTimeFormat())
                        .language(user.getLanguage())
                        .registered(user.getRegistered())
                        .roleCode(user.getRoleCode())
                        .activeRoleCode(user.getActiveRoleCode())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    @AdminOnly
    @GetMapping("/{user-id}")
    public UserResponse selectUser(@PathVariable("user-id") long userId) {

        return UserResponse.builder()
                .user(Optional.ofNullable(userService.selectUser(userId)).map(user -> UserResponse.User.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .info(user.getInfo())
                        .dateTimeFormat(user.getDateTimeFormat())
                        .language(user.getLanguage())
                        .registered(user.getRegistered())
                        .roleCode(user.getRoleCode())
                        .activeRoleCode(user.getActiveRoleCode())
                        .build()).orElse(null))
                .build();
    }

    @AdminOnly
    @DeleteMapping("/{user-id}")
    public EmptyResponse deleteUser(@PathVariable("user-id") long userId) {

        userService.deleteUser(userId);

        return EmptyResponse.builder().build();
    }

    @AdminOnly
    @PutMapping("/{user-id}")
    public UserResponse updateUser(@Validated(User.ValidationUpdate.class) @RequestBody UserRequest user) {

        User currentUser = userService.selectUser(user.getId());
        currentUser.setInfo(user.getInfo());
        currentUser.setName(user.getName());
        currentUser.setDateTimeFormat(user.getDateTimeFormat());
        currentUser.setLanguage(user.getLanguage());
        currentUser.setActiveRoleCode(user.getActiveRoleCode());
        currentUser.setRoleCode(user.getRoleCode());
        userService.updateUser(currentUser);

        return UserResponse.builder()
                .user(UserResponse.User.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .info(user.getInfo())
                        .dateTimeFormat(user.getDateTimeFormat())
                        .language(user.getLanguage())
                        .registered(user.getRegistered())
                        .roleCode(user.getRoleCode())
                        .activeRoleCode(user.getActiveRoleCode())
                        .build()).build();


    }

    @DisableLogin
    @PostMapping("/login")
    public Boolean login(@RequestBody Map<String, String> account, HttpServletRequest request) throws NoSuchAlgorithmException {

        User user = userService.login(account.get("email"), account.get("password"));
        if (user != null) {
            sessionUtil.login(request, user);
            return true;
        }

        return false;
    }

    @DisableLogin
    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        sessionUtil.logout(request);
    }

    @PostMapping("/{userId}/image")
    public FileInfoResponse uploadFile(@PathVariable(value = "userId") long userId, @RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("size") Long size, @RequestParam("type") String type, HttpServletRequest req, UserInfo userInfo) {

        if (!(userInfo.getId() == userId || sessionUtil.isAdmin(req))) {
            throw new ServiceException(ServiceExceptionCode.BAD_REQUEST);
        }

        String fileName = fileStorageService.storeFile(file, req);

        FileInfo fileInfo = FileInfo.builder().userId(userId)
                .path(fileName)
                .name(name)
                .size(size)
                .type("user")
                .uuid(UUID.randomUUID().toString().replaceAll("-", ""))
                .build();

        fileInfoService.createFileInfo(fileInfo);

        return new FileInfoResponse(fileInfo.getId(), fileInfo.getUuid());
    }

}
