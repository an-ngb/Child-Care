package com.example.demo.services;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.RegisterRequestDto;

public interface UserService {

    AbstractResponse login (LoginRequestDto loginRequestDto);

    AbstractResponse logout(String token);

    AbstractResponse register(RegisterRequestDto registerRequestDto);
}
