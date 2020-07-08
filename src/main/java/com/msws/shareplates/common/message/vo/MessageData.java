package com.msws.shareplates.common.message.vo;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {

    private MessageType type;
    private HashMap<String, Object> data;

    public void addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        data.put(key, value);
    }

    public void removeData(String key) {
        if (data != null) {
            data.remove(key);
        }

    }
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum MessageType{
        SHARE_CLOSED("공유 닫힘"),
    	SHARE_STARTED_STATUS_CHANGE("공유시작상태 변경"),
    	CURRENT_PAGE_CHANGE("현재 페이지 변경"),
        USER_JOINED("새로운 사용자 등장"),
        SCREEN_TYPE_REGISTERED("어드민 브라우저의 스크린 타입 등록"),
        USER_STATUS_CHANGE("사용자 상태 변경"),
        USER_KICK_OUT("사용자 제거"),
        USER_BAN("사용자 차단"),
        USER_ALLOWED("사용자 차단 해제"),
        SCROLL_INFO_CHANGED("프로젝터 스크롤 정보 변경"),
        MOVE_SCROLL("스크롤 위치 이동"),
        USER_FOCUS_CHANGE("사용자 포커스 변경"),
        OPTION_CHANGE("쉐어 관련 옵션 변경"),
        CHAT_MESSAGE("채팅");
    	
    	String desc;
    }

}
