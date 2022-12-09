package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {
    private Integer id;
    private String fullName;
    private String role;
    private String email;
    private Integer age;
    private Integer gender;
    private String phone;
    private String certificate;
    private String degree;
    private String expYear;
    private String specialist;
    private String workingAt;
    private String privateWeb;
    private String startWorkAtTime;
    private String endWorkAtTime;
    private String workAt;
    private String avatar;
    private Integer totalFollower;
    private Integer totalFollowing;
    private Integer doctorId;
    private List<PostSearchResultDto> postSearchResultDtoList;
}
