package com.example.demo.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class Pagination {
    public static Page<?> toPage(List<?> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    public static Pageable of(Integer page, Integer size) {
        page = page == null ? 1 : Math.max(page, 1);
        size = size == null || size < 1 ? 9999 : size;
        return PageRequest.of(page - 1, size);
    }
}
