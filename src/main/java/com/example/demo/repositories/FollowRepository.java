package com.example.demo.repositories;

import com.example.demo.entities.Follow;
import com.example.demo.entities.User;

public interface FollowRepository extends AbstractRepository<Follow, Integer> {
    Follow findFollowByFollowedByUserAndTargetUser(User followedByUser, User targetUser);
}
