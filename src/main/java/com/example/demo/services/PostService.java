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

    AbstractResponse interactWithPost(Integer id, InteractWithPostDto interactWithPostDto);

    AbstractResponse getCommentListByPost(Integer id);

    //    AbstractResponse dislike(InteractDto interactDto);
    AbstractResponse interactionCheck(Integer id);

    AbstractResponse getPostByFollowedUsers();
}
