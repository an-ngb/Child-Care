package com.example.demo.services.impl;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.BookingSearchResultDto;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.dtos.InteractDto;
import com.example.demo.entities.*;
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
    private final PostRepository postRepository;

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
                changeUserRoleDto.getPrivateWeb(),
                changeUserRoleDto.getAvatar());

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
    public AbstractResponse clearBookingList(){
        bookingRepository.deleteAll();
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse clearUserList(){
        doctorProfileRepository.deleteAll();
        userProfileRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse clearPost(){
        postRepository.deleteAll();
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse viewAllPost() {
        List<Post> list = postRepository.findAll();
        return new AbstractResponse(list);
    }
}
