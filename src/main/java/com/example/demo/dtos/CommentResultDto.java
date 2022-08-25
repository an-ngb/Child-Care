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
    //This DTO is used to return comment result when search with Specs.
    private Long id;
    private String content;
    private Integer totalLike;
    private Integer totalDislike;
}