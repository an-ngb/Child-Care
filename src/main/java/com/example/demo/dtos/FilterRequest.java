package com.example.demo.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FilterRequest extends PagingRequest {
    private String roleName;
    private Long roleId;
    private String filter;
    private List<String> filterList;
    private Set<String> roleNameList = new HashSet<>();
}
