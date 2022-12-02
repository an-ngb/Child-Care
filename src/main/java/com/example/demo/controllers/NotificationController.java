package com.example.demo.controllers;

import com.example.demo.dtos.MailRequest;
import com.example.demo.services.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "https://child-care.vercel.app")
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationService;

    @PostMapping("/approver/approval")
    public ResponseEntity<Object> approvalNotifyToApprover(@RequestBody MailRequest req) {
        notificationService.notifyToApproverApproval(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/creator/approved")
    public ResponseEntity<Object> approvalNotifyToCreator(@RequestBody MailRequest req) {
        notificationService.notifyToCreatorApproval(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/creator/reject")
    public ResponseEntity<Object> rejectionNotifyToCreator(@RequestBody MailRequest req) {
        notificationService.notifyToCreatorRejection(req);
        return ResponseEntity.ok().build();
    }
}
