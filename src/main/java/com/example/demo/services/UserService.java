package com.example.demo.services;

import com.example.demo.dtos.LoginDto;
import com.example.demo.dtos.LoginRequestDto;

public interface UserService {

    LoginDto login (LoginRequestDto loginRequestDto);

}
