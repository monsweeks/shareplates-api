package com.msws.shareplates.biz.chapter.service;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.repository.ChapterRepository;
import com.msws.shareplates.biz.share.repository.ShareRepository;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ShareRepository shareRepository;

    public List<Chapter> selectChapters(Chapter chapter) {
        return chapterRepository.findByTopicIdOrderByOrderNo(chapter.getTopic().getId());
    }

    public List<Chapter> selectChapters(Long topicId) {
        return chapterRepository.findByTopicIdOrderByOrderNo(topicId);
    }

    public Chapter selectChapter(long chapterId, long topicId) {
        return chapterRepository.findByIdAndTopicId(chapterId, topicId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
    }

    public Chapter createChapter(Chapter chapter) {
        Topic topic = topicRepository.findById(chapter.getTopic().getId()).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        topic.setChapterCount(topic.getChapterCount() + 1);
        topicRepository.save(topic);
        return chapterRepository.save(chapter);
    }

    public Chapter updateChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }


    public void updateChapterOrders(Long topicId, List<Chapter> chapters) {
        chapters.stream().forEach(chapter -> chapterRepository.updateChapterOrder(topicId, chapter.getId(), chapter.getOrderNo()));
    }

    public void deleteChapter(Chapter chapter) {
        Topic topic = topicRepository.findById(chapter.getTopic().getId()).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        topic.setChapterCount(topic.getChapterCount() - 1);
        topic.setPageCount(topic.getPageCount() - chapter.getPages().size());
        topicRepository.save(topic);
        shareRepository.updateCurrentChapterAndPageNull(chapter.getTopic().getId(), chapter.getId());
        chapterRepository.delete(chapter);
    }


}
