package com.example.demo.controllers;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.CommentDto;
import com.example.demo.dtos.PostDto;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private final UserService userService;
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<AbstractResponse> post(@RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postService.post(postDto));
    }

    @PostMapping("/comment")
    public ResponseEntity<AbstractResponse> comment(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok().body(postService.comment(commentDto));
    }
}
