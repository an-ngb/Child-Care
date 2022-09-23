package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.BookingDto;
import com.example.demo.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<AbstractResponse> booking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok().body(bookingService.booking(bookingDto));
    }
}
