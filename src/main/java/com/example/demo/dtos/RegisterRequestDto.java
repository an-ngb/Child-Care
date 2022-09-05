package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    @NotEmpty(message = "Email can not be empty")
    @NotBlank(message = "Email can not be blank")
    @NotNull(message = "Email can not null")
    @Pattern(regexp = "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$", message = "Incorrect email format.")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password can not be blank")
    @NotNull(message = "Password can not null")
    @Pattern(regexp = "^[\\da-zA-Z_@$#]*$", message = "Password must have ")
    private String password;

    private String fullName;

    private String address;

    @Max(65)
    @Min(18)
    private Integer age;

    @Max(2)
    private int gender;

    @NotEmpty(message = "Phone can not be empty")
    @NotBlank(message = "Phone can not be blank")
    @NotNull(message = "Phone can not null")
    @Pattern(regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$", message = "Incorrect phone number")
    private String phone;

    private Boolean disable = false;
}
