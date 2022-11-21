package com.example.demo.services;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.dtos.SearchDto;

public interface UserService {

    AbstractResponse login (LoginRequestDto loginRequestDto);
    AbstractResponse logout(String token);
    AbstractResponse register(RegisterRequestDto registerRequestDto);
    AbstractResponse getUserProfile(Integer id);

    AbstractResponse getDoctorList();
    AbstractResponse getMyProfile();
    AbstractResponse followUser(Integer targetUserId);
    AbstractResponse checkFollow(Integer targetUserId);
    AbstractResponse getFollowListOfLoggedUser();
    AbstractResponse getFollowListOfUser(Integer id);
    AbstractResponse search(SearchDto searchDto);
}
