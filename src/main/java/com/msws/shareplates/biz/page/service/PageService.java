package com.msws.shareplates.biz.page.service;

import com.msws.shareplates.biz.chapter.entity.Chapter;
import com.msws.shareplates.biz.chapter.repository.ChapterRepository;
import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.repository.PageRepository;
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
public class PageService {


    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ShareRepository shareRepository;

    public List<Page> selectPages(long topicId, long chapterId) {
        return pageRepository.findByChapterTopicIdAndChapterIdOrderByOrderNo(topicId, chapterId);
    }

    public Page selectPage(long topicId, long chapterId, long pageId) {
        return pageRepository.findByChapterTopicIdAndChapterIdAndId(topicId, chapterId, pageId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
    }

    public Page createPage(long topicId, long chapterId, Page page) {
        if (chapterId != page.getChapter().getId()) {
            throw new ServiceException(ServiceExceptionCode.BAD_REQUEST);
        }
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        topic.setPageCount(topic.getPageCount() + 1);
        topicRepository.save(topic);

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        chapter.setPageCount(chapter.getPageCount() + 1);
        chapterRepository.save(chapter);

        return pageRepository.save(page);
    }

    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }

    public void updatePageOrders(long topicId, long chapterId, List<Page> pages) {
        pages.stream().forEach(page -> pageRepository.updatePageOrder(topicId, chapterId, page.getId(), page.getOrderNo()));
    }

    public void deletePage(long topicId, long chapterId, long pageId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        topic.setPageCount(topic.getPageCount() - 1);
        topicRepository.save(topic);

        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        chapter.setPageCount(chapter.getPageCount() - 1);
        chapterRepository.save(chapter);

        shareRepository.updateCurrentPageNull(topicId, chapterId, pageId);
        pageRepository.deletePageById(topicId, chapterId, pageId);
    }


}
