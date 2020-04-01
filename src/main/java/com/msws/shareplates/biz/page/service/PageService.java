package com.msws.shareplates.biz.page.service;

import com.msws.shareplates.biz.page.entity.Page;
import com.msws.shareplates.biz.page.repository.PageRepository;
import com.msws.shareplates.biz.topic.entity.Topic;
import com.msws.shareplates.biz.topic.repository.TopicRepository;
import com.msws.shareplates.common.exception.ServiceException;
import com.msws.shareplates.common.exception.code.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PageService {


    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PageRepository pageRepository;

    public List<Page> selectPages(long topicId, long chapterId) {
        return pageRepository.findByChapterTopicIdAndChapterIdOrderByOrderNo(topicId, chapterId);
    }

    public Page selectPage(long topicId, long chapterId, long pageId) {
        return pageRepository.findByChapterTopicIdAndChapterIdAndId(topicId, chapterId, pageId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
    }

    public Page createPage(long topicId, long chapterId, Page page) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        topic.setPageCount(topic.getPageCount() + 1);
        return pageRepository.save(page);
    }

    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }

    @Transactional
    public void updatePageOrders(long topicId, long chapterId, List<Page> pages) {
        pages.stream().forEach(page -> pageRepository.updatePageOrder(topicId, chapterId, page.getId(), page.getOrderNo()));
    }

    @Transactional
    public void deletePage(long topicId, long chapterId, long pageId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXISTS_TOPIC));
        topic.setPageCount(topic.getPageCount() - 1);
        topicRepository.save(topic);
        pageRepository.deletePageById(topicId, chapterId, pageId);
    }


}
