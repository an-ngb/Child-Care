package com.example.demo.services.impl;

import com.example.demo.services.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Value("${childCare.mail.from.personal}")
    private String fromPersonal;


    @Async("executor")
    @Override
    public void sendSimpleMessage(String title, String content, List<String> lstEmail) {
        if (!CollectionUtils.isEmpty(lstEmail)) {
            final var mineMessage = emailSender.createMimeMessage();
            try {
                final var messageHelper = new MimeMessageHelper(mineMessage, true, "UTF-8");
                if (!Strings.isEmpty(fromEmail)) {
                    messageHelper.setFrom(fromEmail, fromPersonal);
                }
                messageHelper.setSubject(title);
                messageHelper.setTo(lstEmail.toArray(String[]::new));
                messageHelper.setText(content, true);
                emailSender.send(mineMessage);
            } catch (final Exception e) {
                log.error("Method<sendSimpleMessage> error at: ", e);
            }
        }
    }
}
