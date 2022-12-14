package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<AbstractResponse> booking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok().body(bookingService.booking(bookingDto));
    }

    @PostMapping("/get-booking-list-by-day")
    public ResponseEntity<AbstractResponse> booking(@RequestBody SearchBookingDto searchBookingDto) {
        return ResponseEntity.ok().body(bookingService.getBookingListByDay(searchBookingDto));
    }

    @PostMapping("/get-booking-list-by-doctor")
    public ResponseEntity<AbstractResponse> getBookingListByDoctor() {
        return ResponseEntity.ok().body(bookingService.getBookingListOfDoctor());
    }

    @PostMapping("/get-booking-list-by-user")
    public ResponseEntity<AbstractResponse> getBookingListByUser() {
        return ResponseEntity.ok().body(bookingService.getBookingListByUser());
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<AbstractResponse> approveBooking(@PathVariable Integer id, @RequestBody InteractDto interactDto) {
        return ResponseEntity.ok().body(bookingService.approveOrDisapproveBooking(id, interactDto));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<AbstractResponse> updateBooking(@PathVariable Integer id, @RequestBody UpdateDto updateDto){
        return ResponseEntity.ok().body(bookingService.updateBooking(id, updateDto));
    }
}
