package com.example.demo.repositories;

import com.example.demo.entities.User;

public interface UserRepository extends AbstractRepository<User, Long> {

    User findByEmail(String email);

    User findByToken(String token);

    User findUserById(Long id);
}
