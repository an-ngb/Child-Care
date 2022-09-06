package com.example.demo.repositories;

import com.example.demo.entities.User;

public interface UserRepository extends AbstractRepository<User, Integer> {

    User findByEmail(String email);
    User findByToken(String token);
    User findUserById(Integer id);
}
