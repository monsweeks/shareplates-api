package com.msws.shareplates.framework.aop;

import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.share.controller.ShareContentsHttpController;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.share.vo.response.ShareInfo;
import com.msws.shareplates.biz.share.vo.response.ShareResponse;
import com.msws.shareplates.biz.statistic.enums.Stat_database;
import com.msws.shareplates.biz.statistic.service.StatServiceIF;
import com.msws.shareplates.biz.topic.service.TopicService;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.user.vo.response.UserResponse;
import com.msws.shareplates.common.code.RoleCode;
import com.msws.shareplates.common.exception.StatDBException;
import com.msws.shareplates.framework.session.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
@Component
public class StatisticAspect {
	
	private final StatServiceIF<?> mainService;
	
	@Autowired
    private ShareService shareService;
	
	@Autowired
    private TopicService topicService;
	
    @Autowired
    private ChapterService chapterService;

	@Autowired
	public StatisticAspect(List<StatServiceIF<?>> services, @Value("${stat.database}") Stat_database database) {
		
		this.mainService = services.stream().filter( e -> e.getName() == database)
				.findFirst()
				.orElseThrow( () ->
						new StatDBException("no stat database selected")
		);
 
	}

	@AfterReturning(value = "execution(* com.msws.shareplates..controller.ShareContentsHttpController.selectShareContent(..))", returning = "retVal")
	public void logShareInfo(Object retVal) throws Throwable {
		
		try {
			ShareInfo data = (ShareInfo)retVal;
			mainService.setData(data);
		}catch(Exception e) {
			log.error("fail to write statistical data : {}", e.getMessage());
		}

	}
	
	@AfterReturning(value = "execution(* com.msws.shareplates..controller.ShareContentsHttpController.updateCurrentPosition(..)) && args(shareId, .., userInfo)", returning = "retVal")
	public void logShareResponse(long shareId, UserInfo userInfo, Object retVal) throws Throwable {
		
		try {
	        Share share = shareService.selectShare(shareId);
	        
	        ShareInfo data = ShareInfo.builder().topic(new TopicResponse(topicService.selectTopic(share.getTopic().getId())))
	        .chapters(chapterService.selectChapters(share.getTopic().getId()).stream()
	                .map(chapter -> ChapterModel.builder().build().buildChapterModel(chapter))
	                .collect(Collectors.toList()))
	        .share(new ShareResponse(share))
	        .users(share.getShareUsers().stream()
	                .filter(ShareContentsHttpController.distinctByKey(shareUser -> shareUser.getUser().getId()))
	                .map(shareUser -> new UserResponse(shareUser.getUser(), share.getAdminUser().getId().equals(shareUser.getUser().getId()) ? RoleCode.ADMIN : RoleCode.MEMBER, shareUser.getStatus(), shareService.selectLastReadyChat(shareId, shareUser.getUser().getId()).getMessage(), shareUser.getBanYn())).collect(Collectors.toList()))
	        .build();
			mainService.setData(data);			
		}catch(Exception e) {
			log.error("fail to write statistical data : {}", e.getMessage());
		}

	}
	
	

	
	
}
