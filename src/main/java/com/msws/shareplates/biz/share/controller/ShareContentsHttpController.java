package com.msws.shareplates.biz.share.controller;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.service.PageService;
import com.msws.shareplates.biz.page.vo.PageModel;
import com.msws.shareplates.biz.page.vo.response.PageResponse;
import com.msws.shareplates.biz.share.entity.Chat;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.entity.ShareUser;
import com.msws.shareplates.biz.share.repository.ShareChapterResponse;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.OptionInfo;
import com.msws.shareplates.biz.share.vo.ScrollInfo;
import com.msws.shareplates.biz.share.vo.request.ChatRequest;
import com.msws.shareplates.biz.share.vo.response.AccessCodeResponse;
import com.msws.shareplates.biz.share.vo.response.ChatResponse;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.share.vo.response.ShareResponse;
import com.msws.shareplates.biz.statistic.service.StatService;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.user.entity.User;
import com.msws.shareplates.biz.user.vo.response.ShareUserResponse;
import com.msws.shareplates.biz.user.vo.response.UserResponse;
import com.msws.shareplates.common.code.ChatTypeCode;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.code.SocketStatusCode;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.common.message.service.ShareMessageService;
import com.msws.shareplates.framework.session.vo.UserInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log
@RestController
@RequestMapping("/api/shares/{share-id}/contents")
public class ShareContentsHttpController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShareMessageService shareMessageService;

    @Autowired
    private StatService statService;

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @ApiOperation(value = "공유 정보 조회 (공유)")
    @GetMapping("")
    public ShareInfo selectShareContent(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {

        Share share = shareService.selectShare(shareId);

        // 어드민으로부터 차단된 유저
        if (shareService.selectIsBanUser(shareId, userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.SHARE_BANNED_USER);
        }

        // 프라이빗 공유의 경우, 엑세스 코드 입력 화면을 통해 이 API 전에 ShareUser 사용자가 만들어져 있어야 접근이 가능
        if (share.getPrivateYn()) {
            ShareUser shareUser = shareService.selectShareUser(share.getId(), userInfo.getId());

            // 프라이빗인데 어드민이면, 사용자 정보 추가
            if (share.getAdminUser().getId().equals(userInfo.getId())) {
                ShareUser info = ShareUser.builder().user(User.builder().id(userInfo.getId()).build())
                        .share(Share.builder().id(share.getId()).build())
                        .status(SocketStatusCode.OFFLINE)
                        .role(RoleCode.ADMIN)
                        .banYn(false).build();
                shareService.createOrUpdateShareUser(info);
            } else if (shareUser == null) {
                throw new ServiceException(ServiceExceptionCode.SHARE_NEED_ACCESS_CODE);
            }
        }

        List<ShareChapterResponse> chapterPageList = chapterService.selectChapters(share.getTopic().getId()).stream().map(chapter -> ShareChapterResponse.builder().build().buildChapterModel(chapter)).collect(Collectors.toList());

        return ShareInfo.builder()
                .topic(new TopicResponse(topicService.selectTopic(share.getTopic().getId())))
                .chapterPageList(share.getAdminUser().getId().equals(userInfo.getId()) ? chapterPageList : null)
                .chapters(chapterService.selectChapters(share.getTopic().getId()).stream()
                        .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
                        .collect(Collectors.toList()))
                .share(new ShareResponse(share))
                .accessCode(AccessCodeResponse.builder().code(share.getAccessCode()).build())
                .users(share.getShareUsers().stream()
                        .filter(distinctByKey(shareUser -> shareUser.getUser().getId()))
                        .map(shareUser -> new ShareUserResponse(shareUser.getUser(), share.getAdminUser().getId().equals(shareUser.getUser().getId()) ? RoleCode.ADMIN : RoleCode.MEMBER, shareUser.getStatus(), shareService.selectLastReadyChat(shareId, shareUser.getUser().getId()).getMessage(), shareUser.getBanYn(), shareUser.getFocusYn())).collect(Collectors.toList()))
                .messages(shareService.selectShareChatList(shareId)
                        .stream()
                        .map(chat -> ChatResponse.builder()
                                .message(chat.getMessage())
                                .creationDate(chat.getCreationDate())
                                .user(UserResponse.User.builder().id(chat.getUser().getId()).name(chat.getUser().getName()).info(chat.getUser().getInfo()).build())
                                .build()).collect(Collectors.toList()))
                .build();
    }


    @ApiOperation(value = "공유 챕터 페이지 목록 조회")
    @GetMapping("/chapters/{chapterId}/pages")
    public PageResponse selectSharePageList(@PathVariable(value = "share-id") long shareId, @PathVariable Long chapterId, UserInfo userInfo) {

        Share share = shareService.selectShare(shareId);

        if (shareService.selectIsBanUser(shareId, userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.SHARE_BANNED_USER);
        }

        return PageResponse.builder()
                .pages(pageService.selectPages(share.getTopic().getId(), chapterId).stream()
                        .map(page -> PageModel.builder().build().buildPageModel(page))
                        .collect(Collectors.toList()))
                .build();
    }

    @ApiOperation(value = "공유 시작")
    @PutMapping("/start")
    public ShareResponse updateStartShare(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.setStartedYn(true);
        shareService.updateShare(share);

        shareMessageService.sendShareStartedChange(shareId, true, userInfo);

        return new ShareResponse(share);
    }


    @ApiOperation(value = "공유 중지")
    @PutMapping("/stop")
    public ShareResponse updateStopShare(@PathVariable(value = "share-id") long shareId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.setStartedYn(false);
        shareService.updateShare(share);

        shareMessageService.sendShareStartedChange(shareId, false, userInfo);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "페이지 이동")
    @PutMapping("/current/chapters/{chapterId}/pages/{pageId}")
    public ShareResponse updateCurrentPosition(@PathVariable(value = "share-id") long shareId, @PathVariable long chapterId, @PathVariable long pageId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        share.setCurrentChapter(Chapter.builder().id(chapterId).build());
        share.setCurrentPage(Page.builder().id(pageId).build());
        shareService.updateShare(share);

        shareMessageService.sendCurrentPageChange(shareId, chapterId, pageId, userInfo);

        // 통계 정보, 페이지 이동됨
        statService.writePageChanged(share.getTopic().getId(), shareId, chapterId, pageId);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "채팅 메세지 생성")
    @PostMapping("/chats/ready")
    public Boolean createReadyChat(@PathVariable(value = "share-id") long shareId, @RequestBody ChatRequest chatRequest, UserInfo userInfo) {

        Chat chat = Chat.builder()
                .type(ChatTypeCode.READY)
                .user(User.builder().id(userInfo.getId()).build())
                .share(Share.builder().id(shareId).build())
                .message(chatRequest.getMessage()).build();
        shareService.createChat(chat);

        shareMessageService.sendChat(shareId, chat.getType(), chatRequest.getMessage(), userInfo);

        return true;
    }

    @ApiOperation(value = "사용자 BAN 시키기")
    @PutMapping("/users/{userId}/ban")
    public Boolean banUser(@PathVariable(value = "share-id") long shareId, @PathVariable long userId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
        shareService.updateShareUserBan(shareId, userId);
        shareMessageService.sendUserBan(shareId, userId, userInfo);

        return true;
    }

    @ApiOperation(value = "사용자 BAN 취소")
    @PutMapping("/users/{userId}/allow")
    public Boolean allowUser(@PathVariable(value = "share-id") long shareId, @PathVariable long userId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
        shareService.updateShareUserAllow(shareId, userId);

        shareMessageService.sendUserAllowed(shareId, userId, userInfo);

        return true;
    }

    @ApiOperation(value = "사용자 내보내기")
    @PutMapping("/users/{userId}/kickOut")
    public Boolean kickOutUser(@PathVariable(value = "share-id") long shareId, @PathVariable long userId, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }
        shareService.updateShareUserKickOut(shareId, userId);
        shareMessageService.sendUserKickOut(shareId, userId, userInfo);

        return true;
    }

    @ApiOperation(value = "스크롤 정보 변경")
    @PutMapping("/scroll")
    public ShareResponse updateScrollInfo(@PathVariable(value = "share-id") long shareId, @RequestBody ScrollInfo scrollInfo, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        shareMessageService.sendScrollInfoChange(shareId, scrollInfo, userInfo);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "스크롤 위치 이동")
    @PutMapping("/scroll/{dir}")
    public ShareResponse moveScroll(@PathVariable(value = "share-id") long shareId, @PathVariable String dir, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        shareMessageService.sendMoveScroll(shareId, dir, userInfo);

        return new ShareResponse(share);
    }

    @ApiOperation(value = "옵션 변경")
    @PutMapping("/option")
    public ShareResponse updateOption(@PathVariable(value = "share-id") long shareId, @RequestBody OptionInfo optionInfo, UserInfo userInfo) {
        Share share = shareService.selectShare(shareId);

        if (!share.getAdminUser().getId().equals(userInfo.getId())) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        shareMessageService.sendOptionChange(shareId, optionInfo.getOptionKey(), optionInfo.getOptionValue(), userInfo);

        return new ShareResponse(share);
    }


}
