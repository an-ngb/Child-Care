package com.example.demo.repositories;

import com.example.demo.entities.GroupPost;
import com.example.demo.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface PostRepository extends AbstractRepository<Post, Integer>{

    List<Post> findPostById(Integer id);
//    List<Post> findAllByOrderByIdAsc();
//    List<Post> findAllByOrderByIdDesc();
//    List<Post> findAllByRoleIdOrderByTotalLikeAsc(Long id);
    List<Post> findAllByUserId(Integer id);
    List<Post> findByGroupPost(GroupPost groupPost);
    List<Post> findByGroupPostOrderById(GroupPost groupPost);
//
//    @Override
//    @EntityGraph(attributePaths = {"comment"})
//    Page<Post> findAll(Specification<Post> spec, Pageable pageable);
//
//    @Override
//    @EntityGraph(attributePaths = {"comment"})
//    List<Post> findAll(Specification<Post> spec);
}
