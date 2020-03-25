package com.msws.shareplates.biz.chapter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.repository.ChapterRepository;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.framework.session.vo.UserInfo;

@Service
public class ChapterService {
	
	@Autowired
	private ChapterRepository chapterRepository;
	
	@Autowired
	private TopicRepository topicRepository;

	public void saveChater(Chapter chapter, UserInfo userInfo) {
		
		topicRepository.findById(chapter.getTopic().getId()).map(topic -> {
						
			if(topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
				throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
			
			chapterRepository.save(chapter);
			
			return topic;
		}).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
		
		
	}
	
	public void deleteChapter(Chapter chapter, UserInfo userInfo) {
		
		topicRepository.findById(chapter.getTopic().getId()).map(topic -> {
			
			if(topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
				throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
			
			chapterRepository.delete(chapter);
			
			return topic;
		}).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
	}
	
	public List<Chapter> getChapters(Chapter chapter, UserInfo userInfo){
		
		return topicRepository.findById(chapter.getTopic().getId()).map(topic -> {
			
			if(topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
				throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);			
			
			return chapterRepository.findByTopicIdOrderByOrderNo(chapter.getTopic().getId());
		}).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
		
	}
	
	public Chapter getChapter(long chapterId, Chapter chapter, UserInfo userInfo) {
		
		return topicRepository.findById(chapter.getTopic().getId()).map(topic -> {
			
			if(topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
				throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);	
			
			return chapterRepository.findById(chapterId);
		}).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC))
				.orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_CHAPTER));
	}
}
