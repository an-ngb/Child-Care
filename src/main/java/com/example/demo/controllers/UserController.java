package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AbstractResponse> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok().body(userService.register(registerRequestDto));
    }

    @PostMapping("/profile/{id}")
    public ResponseEntity<AbstractResponse> getUserProfile(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(userService.getUserProfile(id));
    }

}
