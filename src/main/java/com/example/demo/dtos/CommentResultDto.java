package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResultDto {
    private Integer id;
    private String content;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}