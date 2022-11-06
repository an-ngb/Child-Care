package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final SessionServiceImpl sessionService;
    private final BookingRepository bookingRepository;

    @Override
    public AbstractResponse booking(BookingDto bookingDto) {

        DoctorProfile doctor = doctorProfileRepository.findById(bookingDto.getDoctorId()).orElse(null);
        if(doctor != null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User user = userRepository.findByEmail(authentication.getName());

            if(doctor == null){
                return new AbstractResponse("FAILED", "DOCTOR_NOT_FOUND", 404);
            }

            ZoneId zoneId = ZoneId.systemDefault();

            Booking booking = new Booking(user, doctor.getUser(), bookingDto.getBookedAt().atZone(zoneId).toLocalDate().atStartOfDay().atZone(zoneId).toInstant(), bookingDto.getBookedTime(), bookingDto.getContent(), bookingDto.getShift(), bookingDto.getConsult());

            bookingRepository.save(booking);
        } else {
            return new AbstractResponse("DOCTOR_NOT_FOUND", 404 );
        }
        return new AbstractResponse();
    }

    public BookingSearchResultDto convertBookingToBookingDto(Booking booking){
        BookingSearchResultDto bookingSearchResultDto = new BookingSearchResultDto();
        bookingSearchResultDto.setId(booking.getId());
        bookingSearchResultDto.setCreatedAt(booking.getCreatedAt());
        bookingSearchResultDto.setUpdatedAt(booking.getUpdatedAt());
        bookingSearchResultDto.setCreatedBy(booking.getCreatedBy());
        bookingSearchResultDto.setUpdatedBy(booking.getUpdatedBy());
        bookingSearchResultDto.setDoctorId(booking.getDoctor().getId());
        bookingSearchResultDto.setDoctorName(doctorProfileRepository.findByUser(booking.getDoctor()).getFullName());
        bookingSearchResultDto.setBookedAt(booking.getBookedAt());
        bookingSearchResultDto.setBookedTime(booking.getBookedTime());
        bookingSearchResultDto.setBookedShift(booking.getShiftBooked());
        bookingSearchResultDto.setContent(booking.getContent());
        bookingSearchResultDto.setIsApproved(booking.getIsApproved() == null ? null : booking.getIsApproved());
        return bookingSearchResultDto;
    }

    @Override
    public AbstractResponse getBookingListOfDoctor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName());
        List<Booking> bookingList = bookingRepository.findAllByDoctor(user);
        List<BookingSearchResultDto> bookingSearchResultDtoList = new ArrayList<>();
        bookingList.forEach(item -> {
            bookingSearchResultDtoList.add(convertBookingToBookingDto(item));
        });
        return new AbstractResponse(bookingSearchResultDtoList);
    }

    @Override
    public AbstractResponse approveOrDisapproveBooking(Integer id, InteractDto interactDto) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if(booking == null){
            return new AbstractResponse("FAILED", "BOOKING_SESSION_NOT_FOUND", 404);
        }

        if(booking.getIsApproved() != null && booking.getIsApproved()){
            return new AbstractResponse("FAILED", "BOOKING_SESSION_ALREADY_APPROVED", 400);
        }

        booking.setIsApproved(interactDto.getApprove());
        bookingRepository.save(booking);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse getBookingListByDay(SearchBookingDto searchBookingDto) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(searchBookingDto.getDoctorId()).orElse(null);
        if(doctorProfile != null) {
            List<Booking> bookingList = bookingRepository.findAllByDoctorAndBookedAt(doctorProfile.getUser(), searchBookingDto.getBookedAt().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            List<BookingSearchResultDto> bookingSearchResultDtoList = new ArrayList<>();
            bookingList.forEach(item -> {
                bookingSearchResultDtoList.add(convertBookingToBookingDto(item));
            });
            return new AbstractResponse(bookingSearchResultDtoList);
        } return new AbstractResponse("DOCTOR_NOT_FOUND", 404);
    }

    @Override
    public AbstractResponse getBookingListByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Booking> bookingList = bookingRepository.findAllByCreatedBy(authentication.getName());

        List<BookingSearchResultDto> bookingSearchResultDtoList = new ArrayList<>();
        bookingList.forEach(item -> {
            bookingSearchResultDtoList.add(convertBookingToBookingDto(item));
        });

        return new AbstractResponse(bookingSearchResultDtoList);
    }
}

