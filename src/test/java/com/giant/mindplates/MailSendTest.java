package com.giant.mindplates;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MailSendTest {
	
	@Value("${shareplates.url}")
    private String url;
	
	@Value("${shareplates.mail.senderName}")
	private String mailSender;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	private final String token = "test";
	
	@Test
	public void test() {
		
		try {
			sendEmail("mostgreat@gmail.com", "SHAREPLATES 계정 활성화", getActivationMailContent("activate.html", this.url, token), "logo.png", "logo");
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
	
	public void sendEmail(String receiver, String subject, String contents, String fileName, String attachedResoucreName) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setFrom(mailSender);

        // true = text/html
        helper.setText(contents, true);
        ClassPathResource resource = new ClassPathResource("mail/" + fileName);
        helper.addInline(attachedResoucreName, resource);
        javaMailSender.send(msg);
    }
	
	private String getActivationMailContent(String fileName, String url, String token) throws IOException {

        ClassPathResource resource = new ClassPathResource("mail/" + fileName);

        InputStream is = new FileInputStream(resource.getFile());
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        // TODO 템플릿 엔진으로 변경하면 좋을 듯
        return sb.toString().replaceAll("__origin__", url).replaceAll("__token__", token);
    }

}
