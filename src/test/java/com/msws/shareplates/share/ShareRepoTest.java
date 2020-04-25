package com.msws.shareplates.share;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.service.ChapterService;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.service.PageService;
import com.msws.shareplates.biz.share.entity.Share;
import com.msws.shareplates.biz.share.service.ShareService;
import com.msws.shareplates.biz.topic.entity.Topic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ShareRepoTest {
	
	@Autowired
	private ShareService shareService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private PageService pageService;
	
	
	@Test
	public void test() {
		
		Flag flag = Flag.page;
		
		switch(flag) {
			case  topic :
				selectShareTopicInfo();
				break;
			case page :
				selectPageInfo();
				break;
			case contents :
				selectPageContents();
				break;
		}
	}
	
	public void selectShareTopicInfo() {
		
		Share share = shareService.selectShareInfo(591);
		
		Topic topic = share.getTopic();
		log.debug("result, topic id :  {}", topic.getId());
		
		List<Chapter> chapter = chapterService.selectChapters(topic.getId());
		chapter.stream().forEach( e -> log.info("chapter list : {}", e.getId()));
		
		
	}
	
	
	public void selectPageInfo() {
		
		Share share = shareService.selectShareInfo(591);
		Topic topic = share.getTopic();
		List<Chapter> chapter = chapterService.selectChapters(topic.getId());
		
		List<Page> page = pageService.selectPages(topic.getId(), chapter.get(0).getId());
		page.stream().forEach( e -> log.info("Page list : {}", e.getId()));
		
	}
	
	public void selectPageContents() {
		
	}
	
	private enum Flag {
		topic,
		page,
		contents;
		
		
	}
	
	

}
