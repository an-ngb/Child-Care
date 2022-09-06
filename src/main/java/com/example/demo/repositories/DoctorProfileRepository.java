package com.example.demo.repositories;

import com.example.demo.entities.DoctorProfile;
import com.example.demo.entities.User;
import com.example.demo.entities.UserProfile;

public interface DoctorProfileRepository extends AbstractRepository<DoctorProfile, Integer> {
    DoctorProfile findByUser(User user);
}
