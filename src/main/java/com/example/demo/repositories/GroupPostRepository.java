package com.example.demo.repositories;

import com.example.demo.entities.GroupPost;
import com.example.demo.entities.Post;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface GroupPostRepository extends AbstractRepository<GroupPost, Integer>{

    List<GroupPost> findAllByCreatedBy(String email);

    Optional<GroupPost> findGroupPostById(Integer id);

    List<GroupPost> findAllByParentGroup(Integer id);
}
