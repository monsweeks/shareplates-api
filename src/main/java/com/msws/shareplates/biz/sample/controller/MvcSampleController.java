package com.msws.shareplates.biz.sample.controller;

import com.msws.shareplates.biz.sample.vo.response.SampleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample")
public class MvcSampleController {

    @GetMapping("")
    public SampleResponse sampleMvcRestController() {

        return SampleResponse.builder()
                .name("hi")
                .build();
    }

}
