package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private Integer threadId;
    private String thumbnailImage;
//    private List<String> tagList;
}