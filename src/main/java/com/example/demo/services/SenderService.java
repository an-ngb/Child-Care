package com.example.demo.services;

import com.example.demo.dtos.MailRequest;
import com.example.demo.dtos.MessageDto;

public interface SenderService {
    void sendMessage(MailRequest req, MessageDto messageDto);
}
