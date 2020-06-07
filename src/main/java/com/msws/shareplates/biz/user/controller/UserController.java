package com.msws.shareplates.biz.user.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.msws.shareplates.biz.file.entity.FileInfo;
import com.msws.shareplates.biz.file.service.FileInfoService;
import com.msws.shareplates.biz.file.vo.FileInfoResponse;
import com.msws.shareplates.biz.grp.entity.Grp;
import com.msws.shareplates.biz.grp.service.GrpService;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.service.UserService;
import com.msws.shareplates.biz.user.vo.response.UserManagementResponse;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.mail.MailService;
import com.msws.shareplates.common.util.SessionUtil;
import com.msws.shareplates.common.vo.EmptyResponse;
import com.msws.shareplates.framework.annotation.DisableLogin;
import com.msws.shareplates.framework.aop.annotation.AdminOnly;
import com.msws.shareplates.framework.exception.BizException;
import com.msws.shareplates.framework.session.vo.UserInfo;

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
    public Map selectMyInfo(UserInfo userInfo) {
        Map<String, Object> info = new HashMap<>();

        Long shareCount = shareService.selectOpenShareCount(userInfo != null ? userInfo.getId() : null);
        info.put("shareCount", shareCount);

        if (userInfo == null) {
            info.put("user", null);
            info.put("grps", grpService.selectPublicGrpList());

        } else {
            User user = userService.selectUser(userInfo.getId());
            info.put("user", user);
            List<Grp> grps = grpService.selectUserGrpList(userInfo.getId(), true);
            info.put("grps", grps);
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
        List<Grp> grps = grpService.selectUserGrpList(userId, true);

        info.put("user", user);
        info.put("grps", grps);

        return info;
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
    public UserManagementResponse selectUsers() {
    	userService.selectUserList();
        return UserManagementResponse.builder()
        		.userList(userService.selectUserList().stream().map(user -> UserManagementResponse.User.builder()
    					.email(user.getEmail())
    					.id(user.getId())
    					.name(user.getName())
    					.build()).collect(Collectors.toList()))
        		.build();
    }
    
    @AdminOnly
    @GetMapping("/{user-id}")
    public UserManagementResponse selectUser(@PathVariable("user-id") long userId) {   	
    	
    	return UserManagementResponse.builder()
    			.user(Optional.ofNullable(userService.selectUser(userId)).map(user -> UserManagementResponse.User.builder()
    					.email(user.getEmail())
    					.id(user.getId())
    					.name(user.getName())
    					.build()).orElse(null))
    			.build();
    }
    
    @AdminOnly
    @DeleteMapping("/{user-id}")
    public EmptyResponse deleteUser(@PathVariable("user-id") long userId) {
    	
    	userService.deleteUser(userId);
    	
    	return EmptyResponse.builder().build();
    }
    
    //TODO 수정항목 파악수 수정 필요
    @AdminOnly
    @PutMapping("/{user-id}")
    public EmptyResponse updateUser(@PathVariable("user-id") long userId) {
    	
    	
    	return EmptyResponse.builder().build();
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

        if (userInfo.getId() != userId) {
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
