package com.example.demo.repositories;

import com.example.demo.entities.Tag;

public interface TagRepository extends AbstractRepository<Tag, Long>{
    Tag findByTagName(String tagName);
}
