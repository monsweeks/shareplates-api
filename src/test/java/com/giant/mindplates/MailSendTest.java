package com.giant.mindplates;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MailSendTest {
	
	@Value("${shareplates.mail.senderName}")
	private String mailSender;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Test
	public void test() {
		
		try {
			sendEmail("mostgreat@gmail.com", "test", "test");
		} catch (Exception e) {
			log.error("fail to send Email : {}", e.getMessage());
		}
		
	}
	
	private void sendEmail(String receiver, String subject, String contents) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, false);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(contents);
        helper.setFrom(mailSender);

        javaMailSender.send(msg);
	}

}
