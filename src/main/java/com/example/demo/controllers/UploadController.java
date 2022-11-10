package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ImageAbstractResponse;
import com.example.demo.dtos.PostDto;
import com.example.demo.services.impl.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageAbstractResponse> upload(@ModelAttribute MultipartFile file) {
        return ResponseEntity.ok().body(cloudinaryService.upload(file));
    }

//    @PostMapping("/uploadAvatar")
//    public @ResponseBody
//    String uploadAvatar(@RequestHeader(value = "authToken") String authToken, @RequestHeader(value = "email") String email, @RequestHeader(value="isAvatarSinglePerson") String isAvatarSinglePerson, @RequestParam("file") MultipartFile file) {
//        return cloudinaryService.uploadAvatar(authToken, email, isAvatarSinglePerson, file);
//    }
//
//    @GetMapping("/downloadImg/{publicId}/{width}/{height}")
//    public @ResponseBody
//    ResponseEntity<ByteArrayResource> downloadImg(@PathVariable String publicId, @PathVariable int width, @PathVariable int height) {
//        return cloudinaryService.downloadImg(publicId, width, height, false);
//    }
//
//    @GetMapping("/downloadAvatar/{publicId}/{size}")
//    public @ResponseBody
//    ResponseEntity<ByteArrayResource> downloadAvatar(@PathVariable String publicId, @PathVariable int size) {
//        return cloudinaryService.downloadImg(publicId, size, size, true);
//    }

}
