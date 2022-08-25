package com.example.demo.services;

import com.example.demo.dtos.*;

public interface PostService {

    AbstractResponse post(PostDto postDto);
    AbstractResponse edit(EditDto editDto);
    AbstractResponse comment(CommentDto commentDto);
    AbstractResponse viewAllPost();
    AbstractResponse viewAllPostAsc();
    AbstractResponse viewAllPostDesc();
    AbstractResponse viewMostLikedPost(FilterRequest filterRequest);
    AbstractResponse like(LikeDto likeDto);
}
