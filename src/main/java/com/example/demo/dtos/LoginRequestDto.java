package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @NotEmpty(message = "Email can not be empty")
    @NotBlank(message = "Email can not be blank")
    @NotNull(message = "Email can not null")
    private String email;
    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password can not be blank")
    @NotNull(message = "Password can not be null")
    private String password;
}
