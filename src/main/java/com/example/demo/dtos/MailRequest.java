package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailRequest {
    private List<String> lstEmail;
    private String creator;
    private String receiver;
    private String status;


    public MailRequest(List<String> lstEmail, String creator, String receiver) {
        this.lstEmail = lstEmail;
        this.creator = creator;
        this.receiver = receiver;
    }
}
