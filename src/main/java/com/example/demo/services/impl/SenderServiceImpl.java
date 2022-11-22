package com.example.demo.services.impl;

import com.example.demo.dtos.MailRequest;
import com.example.demo.dtos.MessageDto;
import com.example.demo.dtos.NotificationTemplateDto;
import com.example.demo.services.MailSenderService;
import com.example.demo.services.SenderService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SenderServiceImpl implements SenderService {

    @Autowired
    private MailSenderService mailSenderService;

    public static NotificationTemplateDto parseContentNotification(NotificationTemplateDto template, Instant date, String creator, String receiver){
        final val dateString = DateFormatUtils.format(Date.from(date),"yyyy-MM-dd");
        template.setTitle(parseTitleNotification(template.getTitle(), creator, receiver));
        template.setContent(parseTitleNotification(template.getContent(), creator, receiver));
        template.setDate(template.getDate()
                .replace("XDateX", dateString));
        template.setMailContent(replaceContentTemplate(template));
        return template;
    }

    private static String replaceContentTemplate(NotificationTemplateDto template) {
        return template.getMailContent().replace("XContentX", StringUtils.stripToEmpty(template.getContent()))
                .replace("XPendingX", StringUtils.stripToEmpty(template.getPending()))
                .replace("XStatusX", StringUtils.stripToEmpty(template.getStatus()))
                .replace("XDateX", StringUtils.stripToEmpty(template.getDate()));
    }

    public static String parseTitleNotification(final String title, final String creator, final String receiver){
        return title.replace("XUserX", StringUtils.stripToEmpty(creator))
                .replace("XReceiverX",StringUtils.stripToEmpty(receiver));
    }

    @Override
    public void sendMessage(MailRequest req, MessageDto messageDto) {
        Instant now  = Instant.now();
        final var contentNotification = parseContentNotification(messageDto.getContent(), now, req.getCreator(), req.getReceiver());
        List<String> lstEmail = req.getLstEmail().parallelStream().distinct().collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(req.getLstEmail())){
            log.info("Started send email to: {} and title: {}", lstEmail, contentNotification.getTitle());
            mailSenderService.sendSimpleMessage(contentNotification.getTitle(),contentNotification.getMailContent(),lstEmail);
        }
    }

}
