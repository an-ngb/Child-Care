package com.example.demo.services;

import com.example.demo.dtos.*;

import java.io.IOException;

public interface PostService {

    AbstractResponse post(PostDto postDto) throws IOException;
    AbstractResponse edit(EditDto editDto);
    AbstractResponse comment(CommentDto commentDto);
//    AbstractResponse viewAllPost();
//    AbstractResponse viewAllPostAsc();
//    AbstractResponse viewAllPostDesc();
//    AbstractResponse viewMostLikedPost(FilterRequest filterRequest);
////    AbstractResponse viewPostByTag(FilterRequest filterRequest);
//    AbstractResponse like(InteractDto interactDto);
//    AbstractResponse dislike(InteractDto interactDto);
}
