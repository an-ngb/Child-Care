package com.example.demo.services.impl;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public AbstractResponse changeUserRole(ChangeUserRoleDto changeUserRoleDto) {
        User user = userRepository.findByEmail(changeUserRoleDto.getEmail());
        user.setRole(roleRepository.findRoleByRoleName(changeUserRoleDto.getNewRole()));
        userRepository.save(user);
        return new AbstractResponse();
    }
}
