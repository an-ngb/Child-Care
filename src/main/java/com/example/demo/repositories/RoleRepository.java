package com.example.demo.repositories;

import com.example.demo.entities.Role;

public interface RoleRepository extends AbstractRepository<Role, Integer> {

    Role findRoleById(Integer id);
//
//    Role findRoleByRoleName(String roleName);
}
