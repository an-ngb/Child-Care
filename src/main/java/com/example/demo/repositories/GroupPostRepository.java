package com.example.demo.repositories;

import com.example.demo.entities.GroupPost;
import com.example.demo.entities.Post;

import javax.swing.*;
import java.util.List;

public interface GroupPostRepository extends AbstractRepository<GroupPost, Integer>{

//    Post findPostById(Long id);
//    List<Post> findAllByOrderByIdAsc();
//    List<Post> findAllByOrderByIdDesc();
//    List<Post> findAllByRoleIdOrderByTotalLikeAsc(Long id);
    List<GroupPost> findAllByCreatedBy(String email);

    GroupPost findGroupPostById(Integer id);
//
//    @Override
//    @EntityGraph(attributePaths = {"comment"})
//    Page<Post> findAll(Specification<Post> spec, Pageable pageable);
//
//    @Override
//    @EntityGraph(attributePaths = {"comment"})
//    List<Post> findAll(Specification<Post> spec);
}
