package com.giant.mindplates.biz.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.giant.mindplates.util.MailHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mail/sample")
public class MailSampleController {
	
	@Autowired
	private MailHandler mailHandler;

    @PostMapping("send")
    public String sampleMvcRestController(@RequestParam(required = true, defaultValue = "mostgreat@gmail.com") String receiver, 
    									  @RequestParam(required = true, defaultValue = "test mail from mindplates") String subject,
    									  @RequestParam(required = true, defaultValue = "no contents") String contents) {
    	
    	
    	try {
			mailHandler.sendEmail(receiver, subject, contents);
		} catch (Exception e) {
			log.error("Fail to send email : {}", e.getMessage());			
		} 

        return "mail is sending";
    }

}
