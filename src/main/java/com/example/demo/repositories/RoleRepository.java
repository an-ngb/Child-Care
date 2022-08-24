package com.example.demo.repositories;

import com.example.demo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends AbstractRepository<Role, Long> {

    Role findRoleById(Long id);

    Role findRoleByRoleName(String roleName);
}
