package com.example.demo.services;

import java.util.List;

public interface MailSenderService {
    void sendSimpleMessage(String title, String content, List<String> lstEmail);
}
