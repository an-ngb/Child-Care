package com.example.demo.services.impl;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.entities.Booking;
import com.example.demo.entities.DoctorProfile;
import com.example.demo.entities.User;
import com.example.demo.repositories.*;
import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public AbstractResponse promoteUserToDoctor(ChangeUserRoleDto changeUserRoleDto) {

        User user = userRepository.findByEmail(changeUserRoleDto.getEmail());

        if(user == null){
            return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
        }

        DoctorProfile doctorProfile = new DoctorProfile(user,
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
        if(userProfileRepository.findByUser(user) != null){
            userProfileRepository.delete(userProfileRepository.findByUser(user));
        }
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
        return new AbstractResponse(bookingList);
    }
}
