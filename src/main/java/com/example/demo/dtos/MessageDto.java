package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private NotificationTemplateDto content;
    private String urlRedirectWeb;
    private List<String> lstUserEmailReceiver;


    public static class MessageDtoBuilder{
        public MessageDtoBuilder content(NotificationTemplateDto content){
            this.content = new NotificationTemplateDto(content);
            return this;
        }
    }
}
