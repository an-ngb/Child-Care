package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.BookingDto;
import com.example.demo.dtos.CommentDto;
import com.example.demo.dtos.PostDto;
import com.example.demo.entities.Booking;
import com.example.demo.services.BookingService;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<AbstractResponse> booking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok().body(bookingService.booking(bookingDto));
    }
}
