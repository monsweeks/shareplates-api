package com.giant.mindplates.util;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;



@Component
public class MailHandler {
	
	@Autowired
    private JavaMailSender javaMailSender;

	@Async
	public void sendEmail(String receiver, String subject, String contents) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, false);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(contents);

        javaMailSender.send(msg);
	}
	
	@Async
	public void sendEmail(String receiver, String subject, String contents, String attachedResourcePath, String attachedResoucreName) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(receiver);
        helper.setSubject(subject);

        // true = text/html
        helper.setText(contents, true);

        FileSystemResource file = new FileSystemResource(new File("path/android.png"));
        helper.addAttachment(attachedResoucreName, file);
        javaMailSender.send(msg);
	}
	

}
