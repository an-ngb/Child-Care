package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GatewayController {
    private final UserService userService;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<AbstractResponse> login(@RequestBody @Valid LoginRequestDto loginRequest) {
        try {
            return ResponseEntity.ok().body(userService.login(loginRequest));
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(new AbstractResponse(err));
        }
    }

    @CrossOrigin
    @PostMapping("/logout")
    public ResponseEntity<AbstractResponse> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(userService.logout(token));
    }
}
