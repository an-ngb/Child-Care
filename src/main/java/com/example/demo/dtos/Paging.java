package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paging {
    Integer pageNumber;
    Integer pageSize;
    Integer totalPages;
    Long totalElements;

    public Paging(Page<?> page) {
        this(page.getNumber() + 1, page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
