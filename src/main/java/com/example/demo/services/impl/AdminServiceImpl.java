package com.example.demo.services.impl;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AdminService;
import com.example.demo.utils.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SessionServiceImpl sessionService;

    @Override
    public AbstractResponse changeUserRole(ChangeUserRoleDto changeUserRoleDto) {
        sessionService.isTokenExpire();
        User user = userRepository.findByEmail(changeUserRoleDto.getEmail());
        user.setRole(roleRepository.findRoleByRoleName(changeUserRoleDto.getNewRole()));
        userRepository.save(user);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse getAllUser(){
        List<User> userList = userRepository.findAll();
        return new AbstractResponse(userList);
    }
}
