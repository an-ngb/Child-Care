package com.example.demo.services;

import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.ChangeUserRoleDto;
import com.example.demo.dtos.InteractDto;

public interface AdminService {
    AbstractResponse promoteUserToDoctor(ChangeUserRoleDto changeUserRoleDto);
    AbstractResponse getAllUser();
    AbstractResponse getAllBooking();
    AbstractResponse clearBookingList();
    AbstractResponse clearUserList();
    AbstractResponse clearPost();
    AbstractResponse viewAllPost();
    AbstractResponse clearNullShiftBooking();
}
