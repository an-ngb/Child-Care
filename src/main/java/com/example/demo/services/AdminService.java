package com.example.demo.services;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;

public interface AdminService {
    AbstractResponse promoteUserToDoctor(ChangeUserRoleDto changeUserRoleDto);
    AbstractResponse getAllUser();
    AbstractResponse getAllBooking();
}
