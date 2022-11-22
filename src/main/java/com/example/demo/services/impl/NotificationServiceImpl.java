package com.example.demo.services.impl;

import com.example.demo.dtos.MailRequest;
import com.example.demo.dtos.MessageDto;
import com.example.demo.dtos.NotificationTemplateDto;
import com.example.demo.services.NotificationService;
import com.example.demo.services.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    protected SenderService senderService;

    @Autowired
    @Qualifier("Approve")
    protected NotificationTemplateDto approve;

    @Autowired
    @Qualifier("Rejection")
    protected NotificationTemplateDto rejection;

    @Autowired
    @Qualifier("Approved")
    protected NotificationTemplateDto approved;


    @Async("executor")
    @Override
    public void notifyToCreatorApproval(MailRequest req) {
        if(CollectionUtils.isEmpty(req.getLstEmail())){
            return;
        }
        MessageDto messageDto = MessageDto.builder()
                .content(approved)
                .lstUserEmailReceiver(req.getLstEmail())
                .build();
        senderService.sendMessage(req, messageDto);
    }

    @Async("executor")
    @Override
    public void notifyToApproverApproval(MailRequest req) {
        if(CollectionUtils.isEmpty(req.getLstEmail())){
            return;
        }
        MessageDto messageDto = MessageDto.builder()
                .content(approve)
                .lstUserEmailReceiver(req.getLstEmail())
                .build();
        senderService.sendMessage(req, messageDto);
    }

    @Async("executor")
    @Override
    public void notifyToCreatorRejection(MailRequest req) {
        if(CollectionUtils.isEmpty(req.getLstEmail())){
            return;
        }
        MessageDto messageDto = MessageDto.builder()
                .content(rejection)
                .lstUserEmailReceiver(req.getLstEmail())
                .build();
        senderService.sendMessage(req, messageDto);
    }

}
