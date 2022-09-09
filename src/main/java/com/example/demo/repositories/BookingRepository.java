package com.example.demo.repositories;

import com.example.demo.entities.Booking;
import com.example.demo.entities.GroupPost;
import com.example.demo.entities.User;

import java.util.List;

public interface BookingRepository extends AbstractRepository<Booking, Integer>{
    List<Booking> findAllByDoctor(User doctor);
}
