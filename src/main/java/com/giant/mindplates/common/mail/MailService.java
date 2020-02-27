package com.giant.mindplates.common.mail;

import com.giant.mindplates.util.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.*;

@Service
public class MailService {

    @Value("${shareplates.url}")
    private String url;

    @Autowired
    private MailHandler mailHandler;

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

    public void sendUserActivationMail(String email, String token) throws IOException, MessagingException {
        mailHandler.sendEmail(email, "SHAREPLATES 계정 활성화", getActivationMailContent("activate.html", this.url, token), "logo.png", "logo");
    }


}
