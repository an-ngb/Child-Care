package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostSearchResultDto {
    private Integer id;
    private String title;
    private String content;
    private String author;
    private Integer totalLike;
    private Integer totalDislike;
    private List<CommentResultDto> commentList;
}
