package com.giant.mindplates;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.giant.mindplates.util.MailHandler;


@SpringBootTest
public class MailSendTest {
	
	@Autowired
	private MailHandler mailHander;
	
	@Test
	public void test() {
		
		//mailHander. sendSimpleMessage("test", "test", "test");
		
	}

}
