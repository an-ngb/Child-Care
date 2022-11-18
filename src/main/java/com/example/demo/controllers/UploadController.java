package com.example.demo.controllers;

import com.example.demo.dtos.ImageAbstractResponse;
import com.example.demo.services.impl.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {
    private final CloudinaryService cloudinaryService;
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageAbstractResponse> upload(@ModelAttribute MultipartFile file) {
        return ResponseEntity.ok().body(cloudinaryService.upload(file));
    }
}
