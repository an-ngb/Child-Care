package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
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
    private String thumbnailImage;
    private Instant createAt;
    private Instant updatedAt;
    private List<CommentResultDto> commentList;
}
