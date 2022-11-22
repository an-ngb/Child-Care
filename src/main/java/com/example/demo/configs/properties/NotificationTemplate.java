package com.example.demo.configs.properties;

import com.example.demo.configs.YamlPropertySourceFactory;
import com.example.demo.dtos.NotificationTemplateDto;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:templates/notification/replace-notification.yml", factory = YamlPropertySourceFactory.class)
@Setter
public class NotificationTemplate {

    @Autowired
    protected SpringTemplateEngine templateEngine;

    @Value("${notification.template.common.date}")
    protected String date;

    @Value("${notification.template.approve.title}")
    protected String titleApprove;
    @Value("${notification.template.approve.content}")
    protected String contentApprove;
    @Value("${notification.template.approve.status}")
    protected String statusApprove;

    @Value("${notification.template.rejection.title}")
    protected String titleRejection;
    @Value("${notification.template.rejection.content}")
    protected String contentRejection;
    @Value("${notification.template.rejection.status}")
    protected String statusRejection;

    @Value("${notification.template.approved.title}")
    protected String titleApproved;
    @Value("${notification.template.approved.content}")
    protected String contentApproved;
    @Value("${notification.template.approved.status}")
    protected String statusApproved;

    @Bean("Approve")
    public NotificationTemplateDto getApproveTemplate() {
        String html = templateEngine.process("mail-template", new Context());
        return NotificationTemplateDto.builder()
                .title(titleApprove)
                .content(contentApprove)
                .status(statusApprove)
                .date(date)
                .mailContent(html)
                .build();
    }

    @Bean("Rejection")
    public NotificationTemplateDto getRejectionTemplate() {
        String html = templateEngine.process("mail-template", new Context());
        return NotificationTemplateDto.builder()
                .title(titleRejection)
                .content(contentRejection)
                .status(statusRejection)
                .date(date)
                .mailContent(html)
                .build();
    }

    @Bean("Approved")
    public NotificationTemplateDto getApprovedTemplate() {
        String html = templateEngine.process("mail-template", new Context());
        return NotificationTemplateDto.builder()
                .title(titleApproved)
                .content(contentApproved)
                .status(statusApproved)
                .date(date)
                .mailContent(html)
                .build();
    }
}
