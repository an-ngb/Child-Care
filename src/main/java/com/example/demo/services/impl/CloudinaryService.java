package com.example.demo.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dtos.ImageAbstractResponse;
import com.example.demo.dtos.ImageResponseDto;
import com.example.demo.entities.File;
import com.example.demo.repositories.FileRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Cloudinary cloudinary;

    private final UserService userService;

    private final FileRepository fileRepository;

    private final UserRepository userRepository;

    public ImageAbstractResponse upload(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            File file1 = new File(uploadResult.get("url").toString());
            fileRepository.save(file1);
            ImageResponseDto imageResponseDto = new ImageResponseDto(uploadResult.get("url"));
            return new ImageAbstractResponse(imageResponseDto.getImageUrl());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }
}
