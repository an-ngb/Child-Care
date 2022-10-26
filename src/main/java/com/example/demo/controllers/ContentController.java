package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "http://localhost:1010")
public class ContentController {
    private final UserService userService;
    private final PostService postService;

    @PostMapping(value = "/post", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AbstractResponse> post(@ModelAttribute PostDto postDto) throws IOException {
        return ResponseEntity.ok().body(postService.post(postDto));
    }

    @PostMapping(value = "/post", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AbstractResponse> postV2(@RequestBody PostDto postDto) throws IOException {
        return ResponseEntity.ok().body(postService.post(postDto));
    }

    @PostMapping("/comment")
    public ResponseEntity<AbstractResponse> comment(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok().body(postService.comment(commentDto));
    }

    @PostMapping("/edit")
    public ResponseEntity<AbstractResponse> edit(@RequestBody EditDto editDto){
        return ResponseEntity.ok().body(postService.edit(editDto));
    }

    @PostMapping("/delete")
    public ResponseEntity<AbstractResponse> delete(@RequestBody DeleteDto deleteDto){
        return ResponseEntity.ok().body(postService.deletePost(deleteDto));
    }

    @PostMapping("/search")
    public ResponseEntity<AbstractResponse> search(@RequestBody SearchDto searchDto){
        return ResponseEntity.ok().body(postService.search(searchDto));
    }

//
//    @PostMapping("/dislike")
//    public ResponseEntity<AbstractResponse> dislike(@RequestBody InteractDto interactDto){
//        return ResponseEntity.ok().body(postService.dislike(interactDto));
//    }
//
//    @PostMapping("/read/all")
//    public ResponseEntity<AbstractResponse> viewAllPost(){
//        return ResponseEntity.ok().body(postService.viewAllPost());
//    }
//
//    @PostMapping("/read/all/asc")
//    public ResponseEntity<AbstractResponse> viewAllPostAsc(){
//        return ResponseEntity.ok().body(postService.viewAllPostAsc());
//    }
//
//    @PostMapping("/read/all/desc")
//    public ResponseEntity<AbstractResponse> viewAllPostDesc(){
//        return ResponseEntity.ok().body(postService.viewAllPostDesc());
//    }
//
//    @PostMapping("/read/all/sort-by-most-liked")
//    public ResponseEntity<AbstractResponse> viewAllPostWithMostLiked(@RequestBody FilterRequest filterRequest){
//        return ResponseEntity.ok().body(postService.viewMostLikedPost(filterRequest));
//    }

//    @PostMapping("/search")
//    public ResponseEntity<AbstractResponse> viewPostByTag(@RequestBody FilterRequest filterRequest){
//        return ResponseEntity.ok().body(postService.viewPostByTag(filterRequest));
//    }
}
