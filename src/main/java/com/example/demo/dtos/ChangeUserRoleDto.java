package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleDto {
    private String email;
    private String certificate;
    private String degree;
    private String expYear;
    private String specialist;
    private String workingAt;
    private String privateWeb;
    private String startWorkAtTime;
    private String endWorkAtTime;
    private String workAt;
}