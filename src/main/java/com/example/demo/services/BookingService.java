package com.example.demo.services;

import com.example.demo.dtos.*;

import java.time.Instant;

public interface BookingService {

    AbstractResponse booking(BookingDto bookingDto);

    AbstractResponse getBookingListOfDoctor();

    AbstractResponse approveOrDisapproveBooking(Integer id, InteractDto interactDto);

    AbstractResponse getBookingListByDay(Instant time);

    AbstractResponse getBookingListByUser();

}
