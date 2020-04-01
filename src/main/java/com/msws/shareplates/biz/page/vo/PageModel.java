package com.msws.shareplates.biz.page.vo;

import com.msws.shareplates.biz.page.entity.Page;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
public class PageModel extends RepresentationModel<PageModel> {

    private Long id;
    private String title;
    private String content;
    private int orderNo;
    private long chapterId;

    public PageModel buildPageModel(Page page) {

        id = page.getId();
        title = page.getTitle();
        content = page.getContent();
        orderNo = page.getOrderNo();
        chapterId = page.getChapter().getId();

        return this;
    }
}
