package com.msws.shareplates.biz.chapter.service;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.repository.ChapterRepository;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import com.msws.shareplates.framework.session.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TopicRepository topicRepository;

    public Chapter saveChapter(Chapter chapter, UserInfo userInfo) {
        topicRepository.findById(chapter.getTopic().getId()).map(topic -> {

            if (topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
                throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);


            return topic;
        }).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));

        chapter = chapterRepository.save(chapter);
        return chapter;

    }

    @Transactional
    public void updateChapterOrders(Long topicId, List<Chapter> chapters) {
        chapters.stream().forEach(chapter -> chapterRepository.updateChapterOrder(topicId, chapter.getId(), chapter.getOrderNo()));
    }

    public void deleteChapter(Chapter chapter, UserInfo userInfo) {

        topicRepository.findById(chapter.getTopic().getId()).map(topic -> {

            if (topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
                throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);

            chapterRepository.delete(chapter);

            return topic;
        }).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
    }

    public List<Chapter> getChapters(Chapter chapter, UserInfo userInfo) {

        return topicRepository.findById(chapter.getTopic().getId()).map(topic -> {

            if (topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
                throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);

            return chapterRepository.findByTopicIdOrderByOrderNo(chapter.getTopic().getId());
        }).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));

    }

    public Chapter getChapter(long chapterId, Chapter chapter, UserInfo userInfo) {

        return topicRepository.findById(chapter.getTopic().getId()).map(topic -> {

            if (topic.getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId())))
                throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);

            return chapterRepository.findById(chapterId);
        }).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC))
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_CHAPTER));
    }

    public Chapter getChapter(long chapterId, UserInfo userInfo) {

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_CHAPTER));

        if (chapter.getTopic().getTopicUsers().stream().noneMatch(topicUser -> topicUser.getUser().getId().equals(userInfo.getId()))) {
            throw new ServiceException(ServiceExceptionCode.RESOURCE_NOT_AUTHORIZED);
        }

        return chapter;

    }
}
