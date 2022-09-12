package com.example.demo.repositories;

import com.example.demo.entities.Post;
import com.example.demo.entities.PostFile;

public interface PostFileRepository extends AbstractRepository<PostFile, Integer> {
    PostFile findByPost(Post post);
}
