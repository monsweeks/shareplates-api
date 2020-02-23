package com.giant.mindplates.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Component
public class MailHandler {
	
	@Value("${shareplates.mail.senderName}")
	private String mailSender;

	@Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String receiver, String subject, String contents) throws MessagingException, IOException {

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


}
