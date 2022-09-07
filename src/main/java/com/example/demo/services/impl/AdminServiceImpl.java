package com.example.demo.services.impl;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.BookingSearchResultDto;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.dtos.InteractDto;
import com.example.demo.entities.Booking;
import com.example.demo.entities.DoctorProfile;
import com.example.demo.entities.User;
import com.example.demo.entities.UserProfile;
import com.example.demo.repositories.*;
import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final BookingRepository bookingRepository;
    private final SessionServiceImpl sessionService;
    private final BookingServiceImpl bookingService;

    @Override
    public AbstractResponse promoteUserToDoctor(ChangeUserRoleDto changeUserRoleDto) {

        User user = userRepository.findByEmail(changeUserRoleDto.getEmail());

        if(user == null){
            return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
        }

        DoctorProfile foundDoctorProfile = doctorProfileRepository.findByUser(user);

        if(foundDoctorProfile != null){
            return new AbstractResponse("FAILED", "USER_ALREADY_DOCTOR", 400);
        }

        UserProfile userProfile = userProfileRepository.findByUser(user);

        DoctorProfile doctorProfile = new DoctorProfile(user,
                userProfile.getFullName(),
                changeUserRoleDto.getCertificate(),
                changeUserRoleDto.getDegree(),
                changeUserRoleDto.getExpYear(),
                changeUserRoleDto.getSpecialist(),
                changeUserRoleDto.getWorkAt(),
                changeUserRoleDto.getEndWorkAtTime(),
                changeUserRoleDto.getStartWorkAtTime(),
                changeUserRoleDto.getWorkingAt(),
                changeUserRoleDto.getPrivateWeb());

        user.setRole(roleRepository.findRoleById(2));
        userRepository.save(user);
        doctorProfileRepository.save(doctorProfile);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse getAllUser(){
        List<User> userList = userRepository.findAll();
        return new AbstractResponse(userList);
    }

    @Override
    public AbstractResponse getAllBooking() {
        List<Booking> bookingList = bookingRepository.findAll();
        List<BookingSearchResultDto> bookingSearchResultDtoList = new ArrayList<>();
        bookingList.forEach(item ->{
            BookingSearchResultDto bookingSearchResultDto = bookingService.convertBookingToBookingDto(item);
            bookingSearchResultDtoList.add(bookingSearchResultDto);
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
}
