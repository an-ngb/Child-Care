package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.dtos.SearchDto;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://child-care.vercel.app")
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

    @PostMapping("/my-profile")
    public ResponseEntity<AbstractResponse> getMyProfile() {
        return ResponseEntity.ok().body(userService.getMyProfile());
    }

    @PostMapping("/follow/{id}")
    public ResponseEntity<AbstractResponse> followUser(@PathVariable("id") Integer id){
        return ResponseEntity.ok().body(userService.followUser(id));
    }

    @PostMapping("/follow-check/{id}")
    public ResponseEntity<AbstractResponse> checkFollow(@PathVariable("id") Integer id){
        return ResponseEntity.ok().body(userService.checkFollow(id));
    }

    @PostMapping("/my-follow")
    public ResponseEntity<AbstractResponse> getMyFollowList(){
        return ResponseEntity.ok().body(userService.getFollowListOfLoggedUser());
    }

    @PostMapping("/follow-list/{id}")
    public ResponseEntity<AbstractResponse> getFollowListOfUser(@PathVariable("id") Integer id){
        return ResponseEntity.ok().body(userService.getFollowListOfUser(id));
    }

    @PostMapping("/search")
    public ResponseEntity<AbstractResponse> search(@RequestBody SearchDto searchDto){
        return ResponseEntity.ok().body(userService.search(searchDto));
    }
}
