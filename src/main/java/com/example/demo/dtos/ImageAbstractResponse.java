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
    private Object data;
    private Paging meta = null;
    private Object imageUrl;

    public ImageAbstractResponse(AbstractMap.SimpleEntry<Object, Paging> dataMeta) {
        this.data = dataMeta.getKey();
        this.meta = dataMeta.getValue();
    }

    public ImageAbstractResponse(Object data) {
        this.imageUrl = data;
    }

    public ImageAbstractResponse(Object data, Paging paging) {
        this.data = data;
        this.meta = paging;
    }

    public ImageAbstractResponse(String message) {
        this.message = message;
    }

    public ImageAbstractResponse(String message, int message_code) {
        this.message = message;
        this.message_code = message_code;
    }

    public ImageAbstractResponse(String status, String message, int message_code) {
        this.status = status;
        this.message = message;
        this.message_code = message_code;
    }

    public ImageAbstractResponse(String status, String message, int message_code, Object data) {
        this.status = status;
        this.message = message;
        this.message_code = message_code;
        this.data = data;
    }

    public ImageAbstractResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
