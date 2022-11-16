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
    private Integer userId;
    private Integer totalLike;
    private Integer totalDislike;
    private String thumbnailImage;
    private Long createAt;
    private Long updatedAt;
    private Integer threadId;
    private List<CommentResultDto> commentList;
}
