package com.example.demo.repositories;

import com.example.demo.entities.GroupTag;

public interface GroupTagRepository extends AbstractRepository<GroupTag, Integer>{
    GroupTag findGroupTagByName(String name);
}
