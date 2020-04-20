package com.msws.shareplates.common.message.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {

    private String type;
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

}
