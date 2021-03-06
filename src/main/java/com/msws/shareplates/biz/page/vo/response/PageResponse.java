package com.msws.shareplates.biz.page.vo.response;

import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.page.vo.PageModel;
import com.msws.shareplates.biz.topic.vo.TopicModel;
import com.msws.shareplates.common.code.AuthCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Builder
@Getter
public class PageResponse extends RepresentationModel<PageResponse> {
    private List<PageModel> pages;
    private PageModel page;
    private ChapterModel chapter;
    private TopicModel topic;
    private AuthCode role;
}
