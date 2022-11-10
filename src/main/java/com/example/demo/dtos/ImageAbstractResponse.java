package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.AbstractMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageAbstractResponse {
    private String status = "SUCCESS";
    private String message = "OK";
    private int message_code = 200;
    private String message_type;
    private Paging meta = null;
    private Object imageUrl;

    public ImageAbstractResponse(Object imageUrl) {
        this.imageUrl = imageUrl;
    }
}
