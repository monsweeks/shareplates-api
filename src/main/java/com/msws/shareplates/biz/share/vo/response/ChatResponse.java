package com.msws.shareplates.biz.share.vo.response;

import com.msws.shareplates.biz.user.vo.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatResponse extends RepresentationModel<ChatResponse> {

    private String message;
    private UserResponse.User user;
    private LocalDateTime creationDate;
}
