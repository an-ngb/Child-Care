package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AbstractResponse> register(@ModelAttribute RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok().body(userService.register(registerRequestDto));
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AbstractResponse> registerV2(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok().body(userService.register(registerRequestDto));
    }

    @PostMapping("/profile/{id}")
    public ResponseEntity<AbstractResponse> getUserProfile(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(userService.getUserProfile(id));
    }

}
