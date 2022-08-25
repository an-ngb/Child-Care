package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/change-user-role")
    public ResponseEntity<AbstractResponse> changeUserRole(@RequestBody ChangeUserRoleDto changeUserRoleDto) {
        return ResponseEntity.ok().body(adminService.changeUserRole(changeUserRoleDto));
    }
}
