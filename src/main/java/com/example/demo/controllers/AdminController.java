package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://child-care.vercel.app")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/role/change-user-role")
    public ResponseEntity<AbstractResponse> changeUserRole(@RequestBody ChangeUserRoleDto changeUserRoleDto) {
        return ResponseEntity.ok().body(adminService.promoteUserToDoctor(changeUserRoleDto));
    }

    @PostMapping("/user/all")
    public ResponseEntity<AbstractResponse> getAllUser() {
        return ResponseEntity.ok().body(adminService.getAllUser());
    }

    @PostMapping("/booking/all")
    public ResponseEntity<AbstractResponse> getAllBooking() {
        return ResponseEntity.ok().body(adminService.getAllBooking());
    }

    @PostMapping("/post/all")
    public ResponseEntity<AbstractResponse> getAllPost() {
        return ResponseEntity.ok().body(adminService.viewAllPost());
    }

    @PostMapping("/clear-booking-list")
    public ResponseEntity<AbstractResponse> clearBookingList() {
        return ResponseEntity.ok().body(adminService.clearBookingList());
    }

    @PostMapping("/clear-user")
    public ResponseEntity<AbstractResponse> clearUser() {
        return ResponseEntity.ok().body(adminService.clearUserList());
    }

    @PostMapping("/clear-post")
    public ResponseEntity<AbstractResponse> clearPost() {
        return ResponseEntity.ok().body(adminService.clearPost());
    }

    @PostMapping("/clear-null-shift")
    public ResponseEntity<AbstractResponse> clearNullShiftBooking() {
        return ResponseEntity.ok().body(adminService.clearNullShiftBooking());
    }

    @PostMapping("/clear-reaction")
    public ResponseEntity<AbstractResponse> clearReaction() {
        return ResponseEntity.ok().body(adminService.clearReaction());
    }

    @PostMapping("/clear-null-threadid")
    public ResponseEntity<AbstractResponse> clearNullThreadId() {
        return ResponseEntity.ok().body(adminService.clearNullThreadId());
    }
}
