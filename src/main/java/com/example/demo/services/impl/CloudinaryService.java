package com.example.demo.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dtos.AbstractResponse;
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

    public String upload(MultipartFile file) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                File file1 = new File(uploadResult.get("url").toString());
                fileRepository.save(file1);
                return uploadResult.get("url").toString();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                return null;
            }
        }
    }
//
//    public String uploadAvatar(MultipartFile file, User user) {
//        if (user != null) {
//            String publicId = upload(file, user);
//            userRepository.save(user);
//            return publicId;
//        } else {
//            return null;
//        }
//    }

//    public ResponseEntity<ByteArrayResource> downloadImg(String publicId, int width, int height, boolean isAvatar) {
//
//        logger.info("Requested to download the image file: " + publicId);
//
//        // Generates the URL
//        String format = "jpg";
//        Transformation transformation = new Transformation().width(width).height(height).crop("fill");
//        if (isAvatar) {
//            // transformation = transformation.gravity("face").radius("max");
//            transformation = transformation.radius("max");
//            format = "png";
//        }
//        String cloudUrl = cloudinary.url().secure(true).format(format)
//                .transformation(transformation)
//                .publicId(publicId)
//                .generate();
//
//        logger.debug("Generated URL of the image to be downloaded: " + cloudUrl);
//
//        try {
//            // Get a ByteArrayResource from the URL
//            URL url = new URL(cloudUrl);
//            InputStream inputStream = url.openStream();
//            byte[] out = new byte[inputStream.available()];
//            ByteArrayResource resource = new ByteArrayResource(out);
//
//            // Creates the headers
//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.add("content-disposition", "attachment; filename=image.jpg");
//            responseHeaders.add("Content-Type", "image/jpeg");
//
//            return ResponseEntity.ok()
//                    .headers(responseHeaders)
//                    .contentLength(out.length)
//                    // .contentType(MediaType.parseMediaType(mimeType))
//                    .body(resource);
//
//        } catch (Exception ex) {
//            logger.error("FAILED to download the file: " + publicId);
//            return null;
//        }
//    }
