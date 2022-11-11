package com.example.demo.services;

import com.example.demo.dtos.*;

import java.io.IOException;

public interface PostService {

    AbstractResponse post(PostDto postDto) throws IOException;
    AbstractResponse edit(EditDto editDto);
    AbstractResponse comment(CommentDto commentDto);
    AbstractResponse deletePost(DeleteDto deleteDto);
    AbstractResponse search(SearchDto searchDto);
    AbstractResponse getAllParentGroup();
    AbstractResponse getPostInsideThread(GetPostByThreadDto getPostByThreadDto);
    AbstractResponse getPostByPostId(int id);
    AbstractResponse getPostByLoggedUser(GetPostDto getPostDto);
//    AbstractResponse viewAllPostAsc();
//    AbstractResponse viewAllPostDesc();
//    AbstractResponse viewMostLikedPost(FilterRequest filterRequest);
////    AbstractResponse viewPostByTag(FilterRequest filterRequest);
//    AbstractResponse like(InteractDto interactDto);
//    AbstractResponse dislike(InteractDto interactDto);
}
