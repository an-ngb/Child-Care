package com.example.demo.services;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.CommentDto;
import com.example.demo.dtos.EditDto;
import com.example.demo.dtos.PostDto;

public interface PostService {

    AbstractResponse post(PostDto postDto);
    AbstractResponse edit(EditDto editDto);
    AbstractResponse comment(CommentDto commentDto);
}
