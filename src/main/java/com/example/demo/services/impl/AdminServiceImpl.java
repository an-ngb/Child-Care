package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final GroupTagRepository groupTagRepository;
    private final FollowRepository followRepository;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final BookingRepository bookingRepository;
    private final SessionServiceImpl sessionService;
    private final BookingServiceImpl bookingService;
    private final PostRepository postRepository;

    private final GroupPostRepository groupPostRepository;
    private final FileRepository fileRepository;
    private final ParentGroupRepository parentGroupRepository;
    private final PostServiceImpl postService;
    private final ReactionRepository reactionRepository;

    @Override
    public AbstractResponse promoteUserToDoctor(ChangeUserRoleDto changeUserRoleDto) {
        User user = userRepository.findByEmail(changeUserRoleDto.getEmail());
        if (user == null) {
            return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
        }
        DoctorProfile foundDoctorProfile = doctorProfileRepository.findByUser(user);
        if (foundDoctorProfile != null) {
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
    public AbstractResponse getAllUser() {
        List<User> userList = userRepository.findAll();
        Set<UserProfileDto> userProfileDtoList = new HashSet<>();
        for (User user : userList) {
            UserProfile userProfile = userProfileRepository.findByUser(user);
            UserProfileDto userProfileDto = new UserProfileDto();
            userProfileDto.setId(user.getId());
            userProfileDto.setEmail(user.getEmail());
            userProfileDto.setRole(user.getRole().getRoleName());
            userProfileDto.setFullName(userProfile.getFullName());
            userProfileDto.setAge(userProfile.getAge());
            userProfileDto.setGender(userProfile.getGender());
            userProfileDto.setPhone(userProfile.getPhone());
            List<GroupPost> groupPostList = groupPostRepository.findAllByCreatedBy(user.getEmail());
            List<PostSearchResultDto> postSearchResultDtoList;
            postSearchResultDtoList = postService.convertPostToPostDto(groupPostList);
            userProfileDto.setPostSearchResultDtoList(postSearchResultDtoList);
            DoctorProfile doctorProfile = doctorProfileRepository.findByUser(user);
            if (doctorProfile == null) {
                userProfileDtoList.add(userProfileDto);
            } else {
                userProfileDto.setCertificate(doctorProfile.getCertificate());
                userProfileDto.setDegree(doctorProfile.getDegree());
                userProfileDto.setExpYear(doctorProfile.getExpYear());
                userProfileDto.setSpecialist(doctorProfile.getSpecialist().getSpecialistName());
                userProfileDto.setWorkingAt(doctorProfile.getWorkingAt());
                userProfileDto.setPrivateWeb(doctorProfile.getPrivateWeb());
                userProfileDto.setStartWorkAtTime(doctorProfile.getStartWorkAtTime());
                userProfileDto.setEndWorkAtTime(doctorProfile.getEndWorkAtTime());
                userProfileDto.setWorkAt(doctorProfile.getWorkAt());
            }
            userProfileDtoList.add(userProfileDto);
        }
        return new AbstractResponse(userProfileDtoList);
    }

    @Override
    public AbstractResponse getAllBooking() {
        List<Booking> bookingList = bookingRepository.findAll();
        List<BookingSearchResultDto> bookingSearchResultDtoList = new ArrayList<>();
        bookingList.forEach(item -> {
            BookingSearchResultDto bookingSearchResultDto = bookingService.convertBookingToBookingDto(item);
            bookingSearchResultDtoList.add(bookingSearchResultDto);
        });
        return new AbstractResponse(bookingSearchResultDtoList);
    }

    @Override
    public AbstractResponse clearBookingList() {
        bookingRepository.deleteAll();
        return new AbstractResponse();
    }

    @Transactional
    @Override
    public AbstractResponse clearUserList() {
        clearBookingList();
        clearPost();
        parentGroupRepository.deleteAll();
        userProfileRepository.deleteAll();
        doctorProfileRepository.deleteAll();
        followRepository.deleteAll();
        userRepository.deleteAll();
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse clearPost() {
        reactionRepository.deleteAll();
        postRepository.deleteAll();
        groupPostRepository.deleteAll();
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse viewAllPost() {
        List<GroupPost> list = groupPostRepository.findAll();
        List<PostSearchResultDto> postSearchResultDtoList = postService.convertPostToPostDto(list);
        return new AbstractResponse(postSearchResultDtoList);
    }

    @Override
    public AbstractResponse clearNullShiftBooking() {
        List<Booking> bookingList = bookingRepository.findAll();
        bookingRepository.deleteAll(bookingList.stream().filter(e -> e.getShiftBooked() == null).collect(Collectors.toList()));
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse clearReaction() {
        reactionRepository.deleteAll();
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse clearNullThreadId() {
        List<GroupPost> groupPostList = groupPostRepository.findAll();
        for (GroupPost groupPost : groupPostList) {
            if (groupPost.getParentGroup() != null) {
                continue;
            }
            List<Post> postList = postRepository.findByGroupPost(groupPost);
            postRepository.deleteAll(postList);
            groupPostRepository.delete(groupPost);
        }
        return new AbstractResponse();
    }
}
