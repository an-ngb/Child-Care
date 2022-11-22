package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplateDto {
    private String title;
    private String content;
    private String pending;
    private String status;
    private String date;
    private String mailContent;

    public NotificationTemplateDto(NotificationTemplateDto templateDto) {
        title = templateDto.getTitle();
        content = templateDto.getContent();
        pending = templateDto.getPending();
        status = templateDto.getStatus();
        date = templateDto.getDate();
        mailContent = templateDto.getMailContent();
    }
}
