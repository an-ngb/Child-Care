package com.example.demo.repositories;

import com.example.demo.entities.ParentGroup;

import java.util.List;

public interface ParentGroupRepository extends AbstractRepository<ParentGroup, Integer>{
    List<ParentGroup> findAll();
}
