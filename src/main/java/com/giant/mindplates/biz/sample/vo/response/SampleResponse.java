package com.giant.mindplates.biz.sample.vo.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SampleResponse {
    private String name;
    private String phone;
}
