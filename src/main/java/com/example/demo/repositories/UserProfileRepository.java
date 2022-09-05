package com.example.demo.repositories;

import com.example.demo.entities.User;
import com.example.demo.entities.UserProfile;

public interface UserProfileRepository extends AbstractRepository<UserProfile, Long> {
    UserProfile findByUser(User user);
}
