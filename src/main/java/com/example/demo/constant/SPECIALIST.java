package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum SPECIALIST {
    PEDIATRIC_DENTIST("Pediatric Dentist"),
    OPTOMETRIST("Optometrist"),
    OPHTHALMOLOGIST("Ophthalmologist"),
    ALLERGIST("Allergist"),
    DERMATOLOGIST("Dermatologist");

    private final String specialistName;

    SPECIALIST(String specialistName) {
        this.specialistName = specialistName;
    }
}
