package com.msws.shareplates.common.exception.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ServiceExceptionCode {

    //공통
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "common.resource.not.found"),
    RESOURCE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "common.not.authorized"),
    RESOURCE_NOT_ENOUGH_AUTHORIZED(HttpStatus.FORBIDDEN, "common.not.enough.authorized"),

    //요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "common.error.badRequest"),
    //이메일
    EXIST_EMAIL(HttpStatus.CONFLICT, "user.error.alreadyRegisterd"),

    //세션
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "session.error.expired"),

    //파일
    FILE_ALREADY_EXIST(HttpStatus.CONFLICT, "file.error.uploadAlreadyExists"),
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "file.error.uploadfail"),
    FILE_NOT_ALLOW_EXTENTION(HttpStatus.NOT_ACCEPTABLE, "file.error.uploadextension"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "file.error.resourceNotFound"),

    //토픽
    TOPIC_ALREADY_EXISTS(HttpStatus.CONFLICT, "topic.error.exists"),
    TOPIC_NO_USER_ASSIGNED(HttpStatus.BAD_REQUEST, "topic.error.no.user.assigned"),

    //공유
    SHARE_CANT_STOP_NO_ADMIN_USER(HttpStatus.BAD_REQUEST, "share.cant.no.admin.user"),
    SHARE_NOT_OPENED(HttpStatus.BAD_REQUEST, "share.not.opened"),
    
    //챕터
    NOT_EXISTS_TOPIC(HttpStatus.NOT_FOUND, "chapter.error.not_exists_topic"),
    NOT_EXISTS_CHAPTER(HttpStatus.NOT_FOUND, "chapter.error.not_exists_chapter"),
    NOT_EXISTS_PAGE(HttpStatus.NOT_FOUND, "page.error.not_exists_page"),
    //ORG
    NO_MANAGER_ASSIGNED(HttpStatus.BAD_REQUEST, "grp.no.manager.assigned"),
    NO_EMPTY_ORGANIZATION(HttpStatus.BAD_REQUEST, "grp.no.empty.grp"),
    NO_ADMIN_USER(HttpStatus.FORBIDDEN, "grp.no.admin.user"),
    ;

    private HttpStatus code;
    private String messageCode;
}
