package com.example.demo.services;

import com.example.demo.dtos.MailRequest;
import com.example.demo.dtos.MessageDto;

public interface NotificationService {
    void notifyToCreatorApproval(MailRequest req);
    void notifyToApproverApproval(MailRequest req);
    void notifyToCreatorRejection(MailRequest req);
}
