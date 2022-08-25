package com.example.demo.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRequest {
    private int pageNumber;
    private int pageSize;
}
