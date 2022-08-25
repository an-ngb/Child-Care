package com.example.demo.repositories;

import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;

import java.util.List;

public interface CommentRepository extends AbstractRepository<Comment, Long>{
    List<Comment> findAllByPost(Post post);
}
