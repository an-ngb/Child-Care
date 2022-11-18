package com.example.demo.services;

import com.example.demo.dtos.*;

public interface BookingService {

    AbstractResponse booking(BookingDto bookingDto);

    AbstractResponse getBookingListOfDoctor();

    AbstractResponse approveOrDisapproveBooking(Integer id, InteractDto interactDto);

    AbstractResponse getBookingListByDay(SearchBookingDto searchBookingDto);

    AbstractResponse getBookingListByUser();

    AbstractResponse updateBooking(Integer id, UpdateDto updateDto);
}
