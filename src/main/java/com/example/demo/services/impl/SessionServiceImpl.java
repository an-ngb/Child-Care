package com.example.demo.services.impl;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.SessionService;
import com.example.demo.utils.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;

    @Override
    public Boolean isTokenExpire(){
        Payload payload = (Payload) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User user = userRepository.findByEmail(payload.getEmail());
        if(user.getToken() == null){
            return true;
        }
        return false;
    }
}
