package com.example.demo.controllers;

import com.example.demo.dtos.*;
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

    @PostMapping("/like")
    public ResponseEntity<AbstractResponse> like(@RequestBody InteractDto interactDto){
        return ResponseEntity.ok().body(postService.like(interactDto));
    }

    @PostMapping("/dislike")
    public ResponseEntity<AbstractResponse> dislike(@RequestBody InteractDto interactDto){
        return ResponseEntity.ok().body(postService.dislike(interactDto));
    }

    @PostMapping("/read/all")
    public ResponseEntity<AbstractResponse> viewAllPost(){
        return ResponseEntity.ok().body(postService.viewAllPost());
    }

    @PostMapping("/read/all/asc")
    public ResponseEntity<AbstractResponse> viewAllPostAsc(){
        return ResponseEntity.ok().body(postService.viewAllPostAsc());
    }

    @PostMapping("/read/all/desc")
    public ResponseEntity<AbstractResponse> viewAllPostDesc(){
        return ResponseEntity.ok().body(postService.viewAllPostDesc());
    }

    @PostMapping("/read/all/sort-by-most-liked")
    public ResponseEntity<AbstractResponse> viewAllPostWithMostLiked(@RequestBody FilterRequest filterRequest){
        return ResponseEntity.ok().body(postService.viewMostLikedPost(filterRequest));
    }

    @PostMapping("/search")
    public ResponseEntity<AbstractResponse> viewPostByTag(@RequestBody FilterRequest filterRequest){
        return ResponseEntity.ok().body(postService.viewPostByTag(filterRequest));
    }
}
