package com.example.demo.repositories;

import com.example.demo.entities.DoctorProfile;
import com.example.demo.entities.User;

import java.util.Optional;

public interface DoctorProfileRepository extends AbstractRepository<DoctorProfile, Integer> {
    DoctorProfile findByUser(User user);

    Optional<DoctorProfile> findById(Integer id);
}
