package com.msws.shareplates.biz.share.vo.response;

import com.msws.shareplates.biz.chapter.vo.ChapterModel;
import com.msws.shareplates.biz.topic.vo.response.TopicResponse;
import com.msws.shareplates.biz.user.vo.response.UserResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Builder
@Data
public class ShareInfo extends RepresentationModel<ShareInfo> {

    private TopicResponse topic;
    private List<ChapterModel> chapters;
    private AccessCodeResponse accessCode;
    private ShareResponse share;
    private List<UserResponse> users;
    private Boolean access;
}
