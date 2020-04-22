package com.msws.shareplates.framework.session.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private long id;
    private Boolean registered;
}
