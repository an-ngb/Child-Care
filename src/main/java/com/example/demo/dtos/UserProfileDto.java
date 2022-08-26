package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileDto {
    private Long id;
    private String fullName;
    private String email;
    private Integer age;
    private Integer gender;
    private String phone;
    private Integer totalPost;
    private Integer totalComment;
    private Integer totalLike;
    private Integer totalDislike;
    private List<PostSearchResultDto> postSearchResultDtoList;
}
