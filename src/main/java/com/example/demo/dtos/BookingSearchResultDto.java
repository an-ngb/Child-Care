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
public class BookingSearchResultDto {
    private Integer id;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer doctorId;
    private String doctorName;
    private Instant bookedAt;
    private Integer bookedTime;
    private String content;
    private Boolean isApproved;
}