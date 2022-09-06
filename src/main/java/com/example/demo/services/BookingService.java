package com.example.demo.services;

import com.example.demo.dtos.*;

public interface BookingService {

    AbstractResponse booking(BookingDto bookingDto);
}
