package com.giant.mindplates.biz.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giant.mindplates.biz.sample.vo.response.SampleResponse;

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
