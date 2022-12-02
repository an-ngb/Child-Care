package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.services.BookingService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://child-care.vercel.app")
public class DoctorController {
    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping("/booking/gets")
    public ResponseEntity<AbstractResponse> getBookingList() {
        return ResponseEntity.ok().body(bookingService.getBookingListOfDoctor());
    }

    @PostMapping("/get-doctor-list")
    public ResponseEntity<AbstractResponse> getDoctorList() {
        return ResponseEntity.ok().body(userService.getDoctorList());
    }
}
