package com.example.demo.repositories;

import com.example.demo.entities.Post;
import com.example.demo.entities.Reaction;

import java.util.List;

public interface ReactionRepository extends AbstractRepository<Reaction, Integer>{
    List<Reaction> findAllByPost(Post post);
}
