package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResultDto {
    private Integer id;
    private String content;
    private String createdBy;
    private Integer userId;
    private Long createdAt;
    private Long updatedAt;
    private Boolean isDoctor;
}